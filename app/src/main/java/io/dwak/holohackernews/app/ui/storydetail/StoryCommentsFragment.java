package io.dwak.holohackernews.app.ui.storydetail;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
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

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.manager.HackerNewsCallback;
import io.dwak.holohackernews.app.manager.HackerNewsException;
import io.dwak.holohackernews.app.manager.HackerNewsManager;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import io.dwak.holohackernews.app.network.models.ReadabilityArticle;
import io.dwak.holohackernews.app.ui.BaseFragment;
import io.dwak.holohackernews.app.widget.ObservableWebView;
import io.dwak.holohackernews.app.widget.ReboundRevealRelativeLayout;
import io.dwak.holohackernews.app.widget.SmoothSwipeRefreshLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StoryCommentsFragment extends BaseFragment implements ObservableWebView.OnScrollChangedCallback {
    private static final String STORY_ID = "story_id";
    private static final String TAG = StoryCommentsFragment.class.getSimpleName();
    public static final String HACKER_NEWS_ITEM_BASE_URL = "https://news.ycombinator.com/item?id=";
    public static final String HACKER_NEWS_BASE_URL = "https://news.ycombinator.com/";
    private final int DISTANCE_TO_HIDE_ACTIONBAR = 1;
    private long mStoryId;
    private int mPrevVisibleItem;
    private HeaderViewHolder mHeaderViewHolder;
    private ActionBar mActionBar;
    private CommentsListAdapter mListAdapter;
    private NodeHNAPIStoryDetail mNodeHNAPIStoryDetail;
    private Bundle mWebViewBundle;
    private boolean mReadability;

    @InjectView(R.id.swipe_container) SmoothSwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.comments_list) ListView mCommentsListView;
    @InjectView(R.id.open_link) Button mOpenLinkDialogButton;
    @InjectView(R.id.story_web_view) ObservableWebView mWebView;
    @InjectView(R.id.link_layout) ReboundRevealRelativeLayout mLinkLayout;
    @InjectView(R.id.fabbutton) FloatingActionButton mFloatingActionButton;
    private StoryDetail mStoryDetail;

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

//        mFloatingActionButton.setDrawable(getResources().getDrawable(R.drawable.ic_action_readability));
//        mFloatingActionButton.setColor(getResources().getColor(R.color.system_bar_tint));
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
        mLinkLayout.setTranslateDirection(ReboundRevealRelativeLayout.TRANSLATE_DIRECTION_VERTICAL);
        mLinkLayout.setOpen(false);

        final ProgressBar progressBar = (ProgressBar) mLinkLayout.findViewById(R.id.progress_bar);
        Button closeLink = (Button) mLinkLayout.findViewById(R.id.close_link);
        closeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        List<NodeHNAPIComment> nodeHNAPICommentList = new ArrayList<NodeHNAPIComment>();

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
        mListAdapter = new CommentsListAdapter(getActivity(), R.layout.comments_list_item, mStoryDetail);
        View headerView = inflater.inflate(R.layout.comments_header, null);
        mHeaderViewHolder = new HeaderViewHolder(headerView);

        mCommentsListView.setHeaderDividersEnabled(false);
        mCommentsListView.addHeaderView(headerView);
        mCommentsListView.setAdapter(mListAdapter);


        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SmoothSwipeRefreshLayout.OnRefreshListener() {
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
        HackerNewsManager.getInstance().getItemDetails(mStoryId, new HackerNewsCallback<StoryDetail>() {
            @Override
            public void onResponse(StoryDetail response, HackerNewsException exception) {
                if (exception == null) {
                    Log.d(TAG, response.toString());
                    mStoryDetail = response;
                    mHeaderViewHolder.mStoryTitle.setText(mStoryDetail.getTitle());
                    mHeaderViewHolder.mStorySubmitter.setText(mStoryDetail.getUser());
                    if (!"job".equals(mStoryDetail.getType())) {
                        mHeaderViewHolder.mContent.setVisibility(View.GONE);
                        if ("link".equals(mStoryDetail.getType())) {
                            String domain = mStoryDetail.getDomain();
                            mHeaderViewHolder.mStoryDomain.setVisibility(View.VISIBLE);
                            mHeaderViewHolder.mStoryDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20));
                            if (mWebViewBundle == null) {
                                mWebView.loadUrl(mStoryDetail.getUrl());
                            }
                            else {
                                mWebView.restoreState(mWebViewBundle);
                            }
                        }
                        else if ("ask".equals(mStoryDetail.getType())) {
                            mHeaderViewHolder.mStoryDomain.setVisibility(View.GONE);

                            mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                            Spanned jobContent = Html.fromHtml(mStoryDetail.getContent());
                            mHeaderViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
                            mHeaderViewHolder.mContent.setText(jobContent);
                        }
                        mHeaderViewHolder.mStoryPoints.setText(String.valueOf(mStoryDetail.getPoints()));
                        mHeaderViewHolder.mStoryLongAgo.setText(" | " + mStoryDetail.getTimeAgo());
                        mHeaderViewHolder.mCommentsCount.setText(mStoryDetail.getCommentsCount() + " comments");
                    }
                    else {
                        mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                        Spanned jobContent = Html.fromHtml(mStoryDetail.getContent());
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
                                mActionBar.setTitle(mStoryDetail.getTitle());
                            }
                        }
                    });

                    mOpenLinkDialogButton.setVisibility(View.VISIBLE);
                    mOpenLinkDialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLinkLayout.setOpen(!mLinkLayout.isOpen());
                            if ("ask".equals(mStoryDetail.getType())) {
                                mStoryDetail.setUrl(HACKER_NEWS_BASE_URL + mStoryDetail.getUrl());
                            }
                            else if ("job".equals(mStoryDetail.getType())) {
                                if (mStoryDetail.getUrl().contains("/item?id=")) {
                                    mStoryDetail.setUrl(HACKER_NEWS_BASE_URL + mStoryDetail.getUrl());
                                }
                            }
                        }
                    });

                    mListAdapter.clear();
                    mListAdapter.addAll(mStoryDetail.getCommentList());
                    showProgress(false);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else {
                    Log.d(TAG, exception.toString());
                    Toast.makeText(getActivity(), "Problem loading page", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
                                sendIntent.putExtra(Intent.EXTRA_TEXT, mNodeHNAPIStoryDetail.getUrl());
                                break;
                            case 1:
                                sendIntent.putExtra(Intent.EXTRA_TEXT, HACKER_NEWS_ITEM_BASE_URL + mStoryId);
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
                browserIntent.setData(Uri.parse(HACKER_NEWS_ITEM_BASE_URL + mStoryId));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isLinkViewVisible(){
       return mLinkLayout.isOpen();
    }

    public void hideLinkView(){
        mLinkLayout.setOpen(false);
    }
    private void readability() {
        mReadability = !mReadability;
        if (mReadability) {
            mReadabilityService.getReadabilityForArticle(HoloHackerNewsApplication.getREADABILITY_TOKEN(),
                    mNodeHNAPIStoryDetail.getUrl(),
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
            mWebView.loadUrl(mNodeHNAPIStoryDetail.getUrl());
        }
    }

    @Override
    public void onScroll(int l, int t, int oldL, int oldT) {
        if(t >= oldT) {
            mFloatingActionButton.hide();
        }
        else {
            mFloatingActionButton.show();
        }
    }

    static class HeaderViewHolder {
        @InjectView(R.id.story_title) TextView mStoryTitle;
        @InjectView(R.id.story_domain) TextView mStoryDomain;
        @InjectView(R.id.story_submitter) TextView mStorySubmitter;
        @InjectView(R.id.story_points) TextView mStoryPoints;
        @InjectView(R.id.story_long_ago) TextView mStoryLongAgo;
        @InjectView(R.id.comment_count) TextView mCommentsCount;
        @InjectView(R.id.job_content)TextView mContent;

        public HeaderViewHolder(View headerView) {
            ButterKnife.inject(this, headerView);
        }
    }
}
