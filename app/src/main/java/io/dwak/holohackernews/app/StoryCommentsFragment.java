package io.dwak.holohackernews.app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.network.models.Comment;
import io.dwak.holohackernews.app.network.models.ReadabilityArticle;
import io.dwak.holohackernews.app.network.models.StoryDetail;
import io.dwak.holohackernews.app.widget.ObservableWebView;
import io.dwak.holohackernews.app.widget.ReboundRevealRelativeLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StoryCommentsFragment extends BaseFragment implements ObservableWebView.OnScrollChangedCallback {
    private static final String STORY_ID = "story_id";
    private static final String TAG = StoryCommentsFragment.class.getSimpleName();
    private final int DISTANCE_TO_HIDE_ACTIONBAR = 1;
    private long mStoryId;
    private int mPrevVisibleItem;
    private HeaderViewHolder mHeaderViewHolder;
    private ActionBar mActionBar;
    private CommentsListAdapter mListAdapter;
    private StoryDetail mStoryDetail;
    private Bundle mWebViewBundle;
    private OnStoryFragmentInteractionListener mListener;
    private boolean mReadability;

    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.comments_list) ListView mCommentsListView;
    @InjectView(R.id.open_link) Button mOpenLinkDialogButton;
    @InjectView(R.id.story_web_view) ObservableWebView mWebView;
    @InjectView(R.id.link_layout) ReboundRevealRelativeLayout mLinkLayout;
    @InjectView(R.id.fabbutton) FloatingActionButton mFloatingActionButton;

    public StoryCommentsFragment() {
        // Required empty public constructor
    }

    public static StoryCommentsFragment newInstance(long param1) {
        StoryCommentsFragment fragment = new StoryCommentsFragment();
        Bundle args = new Bundle();
        args.putLong(STORY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoryId = getArguments().getLong(STORY_ID);
        }
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story_comments, container, false);
        ButterKnife.inject(this, rootView);

        mFloatingActionButton.setDrawable(getResources().getDrawable(R.drawable.ic_action_readability));
        mFloatingActionButton.setColor(getResources().getColor(R.color.system_bar_tint));
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readability();
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mLinkLayout.setStashPixel(0);
        mLinkLayout.setRevealPixel(height);
        mLinkLayout.setTranslateDirection(ReboundRevealRelativeLayout.TranslateDirection.TRANSLATE_DIRECTION_VERTICAL);
        mLinkLayout.setOpen(false);

        final ProgressBar progressBar = (ProgressBar) mLinkLayout.findViewById(R.id.progress_bar);
        Button closeLink = (Button) mLinkLayout.findViewById(R.id.close_link);
        closeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mListener.onStoryLinkFragmentInteraction();
                mLinkLayout.setOpen(false);
            }
        });

        mWebView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setMax(100);
                progressBar.setProgress(newProgress);

            }
        });

        mWebView.setOnScrollChangedCallback(this);

        Button backButton = (Button) rootView.findViewById(R.id.web_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });
        Button forwardButton = (Button) rootView.findViewById(R.id.web_forward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
            }
        });
        mPrevVisibleItem = 1;

        mActionBar = getActivity().getActionBar();
        mActionBar.show();
        mActionBar.setTitle("Hacker News");
        List<Comment> commentList = new ArrayList<Comment>();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mContainer = rootView.findViewById(R.id.container);
        showProgress(true);

        mCommentsListView = (ListView) rootView.findViewById(R.id.comments_list);

        Button previousTopLevelButton = (Button) rootView.findViewById(R.id.prev_top_level);
        previousTopLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentIndex = mCommentsListView.getFirstVisiblePosition() - 1;
                for (int i = currentIndex - 1; i >= 0; i--) {
                    if (mListAdapter.getItem(i).getLevel() == 0) {
                        if (HoloHackerNewsApplication.isDebug()) {
                            Log.d(TAG, String.valueOf(i));
                        }
                        mCommentsListView.setSelectionFromTop(i, 0);
                        return;
                    }
                }
            }
        });
        Button nextTopLevelButton = (Button) rootView.findViewById(R.id.next_top_level);
        nextTopLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentIndex = mCommentsListView.getFirstVisiblePosition() + 1;
                for (int i = currentIndex + 1; i < mListAdapter.getCount(); i++) {
                    if (mListAdapter.getItem(i).getLevel() == 0) {
                        if (HoloHackerNewsApplication.isDebug()) {
                            Log.d(TAG, String.valueOf(i));
                        }
                        mCommentsListView.setSelectionFromTop(i, 0);
                        return;
                    }
                }
            }
        });
        mListAdapter = new CommentsListAdapter(getActivity(), R.layout.comments_list_item, commentList);
        mHeaderViewHolder = new HeaderViewHolder();
        View headerView = inflater.inflate(R.layout.comments_header, null);
        mHeaderViewHolder.mStoryTitle = (TextView) headerView.findViewById(R.id.story_title);
        mHeaderViewHolder.mStoryDomain = (TextView) headerView.findViewById(R.id.story_domain);
        mHeaderViewHolder.mStorySubmitter = (TextView) headerView.findViewById(R.id.story_submitter);
        mHeaderViewHolder.mStoryPoints = (TextView) headerView.findViewById(R.id.story_points);
        mHeaderViewHolder.mStoryLongAgo = (TextView) headerView.findViewById(R.id.story_long_ago);
        mHeaderViewHolder.mCommentsCount = (TextView) headerView.findViewById(R.id.comment_count);
        mHeaderViewHolder.mContent = (TextView) headerView.findViewById(R.id.job_content);

        mCommentsListView.setHeaderDividersEnabled(false);
        mCommentsListView.addHeaderView(headerView);
        mCommentsListView.setAdapter(mListAdapter);


        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });

        refresh();
        return rootView;
    }

    private void refresh() {
        mHackerNewsService.getItemDetails(mStoryId, new Callback<StoryDetail>() {

            @Override
            public void success(final StoryDetail storyDetail, Response response) {
                mStoryDetail = storyDetail;
                mHeaderViewHolder.mStoryTitle.setText(storyDetail.getTitle());
                mHeaderViewHolder.mStorySubmitter.setText(storyDetail.getUser());
                if (!"job".equals(storyDetail.getType())) {
                    mHeaderViewHolder.mContent.setVisibility(View.GONE);
                    if ("link".equals(storyDetail.getType())) {
                        String domain = storyDetail.getDomain();
                        mHeaderViewHolder.mStoryDomain.setVisibility(View.VISIBLE);
                        mHeaderViewHolder.mStoryDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20));
                        if (mWebViewBundle == null) {
                            mWebView.loadUrl(storyDetail.getUrl());
                        }
                        else {
                            mWebView.restoreState(mWebViewBundle);
                        }
                    }
                    else if ("ask".equals(storyDetail.getType())) {
                        mHeaderViewHolder.mStoryDomain.setVisibility(View.GONE);

                        mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                        Spanned jobContent = Html.fromHtml(storyDetail.getContent());
                        mHeaderViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
                        mHeaderViewHolder.mContent.setText(jobContent);
                    }
                    mHeaderViewHolder.mStoryPoints.setText(String.valueOf(storyDetail.getPoints()));
                    mHeaderViewHolder.mStoryLongAgo.setText(" | " + storyDetail.getTimeAgo());
                    mHeaderViewHolder.mCommentsCount.setText(storyDetail.getCommentsCount() + " comments");
                }
                else {
                    mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                    Spanned jobContent = Html.fromHtml(storyDetail.getContent());
                    mHeaderViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
                    mHeaderViewHolder.mContent.setText(jobContent);
                    mHeaderViewHolder.mStoryDomain.setVisibility(View.GONE);
                    mHeaderViewHolder.mCommentsCount.setVisibility(View.GONE);
                    mHeaderViewHolder.mStoryPoints.setVisibility(View.GONE);

                }
                mCommentsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                        if (firstVisibleItem < mPrevVisibleItem - DISTANCE_TO_HIDE_ACTIONBAR) {
//                            setActionbarVisibility(true);
                            mPrevVisibleItem = firstVisibleItem;
                        }
                        else if (firstVisibleItem > mPrevVisibleItem + DISTANCE_TO_HIDE_ACTIONBAR) {
//                            setActionbarVisibility(false);
                            mPrevVisibleItem = firstVisibleItem;
                        }
                        if (firstVisibleItem == 0) {
                            mActionBar.setTitle("Hacker News");
                        }
                        else {
                            mActionBar.setTitle(storyDetail.getTitle());
                        }
                    }
                });
                mOpenLinkDialogButton.setVisibility(View.VISIBLE);
                mOpenLinkDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLinkLayout.setOpen(!mLinkLayout.isOpen());
                        if ("ask".equals(storyDetail.getType())) {
//                            mListener.onStoryFragmentInteraction("https://news.ycombinator.com/" + storyDetail.getUrl());
                            storyDetail.setUrl("https://news.ycombinator.com/" + storyDetail.getUrl());
                        }
                        else if ("job".equals(storyDetail.getType())) {
                            if (storyDetail.getUrl().contains("/item?id=")) {
//                                mListener.onStoryFragmentInteraction("https://news.ycombinator.com/" + storyDetail.getUrl());
                                storyDetail.setUrl("https://news.ycombinator.com/" + storyDetail.getUrl());
                            }
                            else {
//                                mListener.onStoryFragmentInteraction(storyDetail.getUrl());
                            }
                        }
                        else {
//                            mListener.onStoryFragmentInteraction(storyDetail.getUrl());
                        }
                    }
                });
                mListAdapter.setStoryDetail(storyDetail);
                mListAdapter.setComments(storyDetail.getCommentList());
                mListAdapter.notifyDataSetChanged();
                showProgress(false);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
                Toast.makeText(getActivity(), "Problem loading page", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setActionbarVisibility(boolean visible) {
        ((MainActivity) getActivity()).setActionbarVisible(visible);
        Log.d(TAG, String.valueOf(visible));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStoryFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebViewBundle = new Bundle();
        mWebView.saveState(mWebViewBundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            mWebViewBundle = savedInstanceState;
            mWebView.restoreState(mWebViewBundle);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onStoryCommentsFragmentDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.story_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                final CharSequence[] shareItems = {"Link", "Comments"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(shareItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        switch (i) {
                            case 0:
                                sendIntent.putExtra(Intent.EXTRA_TEXT, mStoryDetail.getUrl());
                                break;
                            case 1:
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://news.ycombinator.com/item?id=" + mStoryId);
                                break;
                        }
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });

                builder.create().show();
                break;
            case R.id.action_open_browser:
                Intent browserIntent = new Intent();
                browserIntent.setAction(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://news.ycombinator.com/item?id=" + mStoryId));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readability() {
        mReadability = !mReadability;
        if (mReadability) {
            mReadabilityService.getReadabilityForArticle(HoloHackerNewsApplication.getREADABILITY_TOKEN(),
                    mStoryDetail.getUrl(),
                    new Callback<ReadabilityArticle>() {
                        @Override
                        public void success(ReadabilityArticle readabilityArticle, Response response) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("<HTML><HEAD><LINK href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                            stringBuilder.append("<h1>")
                                    .append(readabilityArticle.getTitle())
                                    .append("</h1>");
                            stringBuilder.append(readabilityArticle.getContent());
                            stringBuilder.append("</body></HTML>");
                            mWebView.loadDataWithBaseURL("file:///android_asset/", stringBuilder.toString(), "text/html", "utf-8", null);
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
        }
        else {
            mWebView.loadUrl(mStoryDetail.getUrl());
        }
    }

    @Override
    public void onScroll(int l, int t, int oldL, int oldT) {
        mFloatingActionButton.hide(t >= oldT);
    }


    public interface OnStoryFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onStoryFragmentInteraction(String url);

        public void onStoryCommentsFragmentDetach();
    }

    static class HeaderViewHolder {
        TextView mStoryTitle;
        TextView mStoryDomain;
        TextView mStorySubmitter;
        TextView mStoryPoints;
        TextView mStoryLongAgo;
        TextView mCommentsCount;
        TextView mContent;
    }
}
