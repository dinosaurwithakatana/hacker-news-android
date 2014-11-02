package io.dwak.holohackernews.app.ui.storydetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.ui.BaseFragment;
import io.dwak.holohackernews.app.widget.ObservableWebView;
import io.dwak.holohackernews.app.widget.ReboundRevealRelativeLayout;
import io.dwak.holohackernews.app.widget.SmoothSwipeRefreshLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoryDetailFragment extends BaseFragment implements ObservableWebView.OnScrollChangedCallback {
    public static final String HACKER_NEWS_ITEM_BASE_URL = "https://news.ycombinator.com/item?id=";
    public static final String HACKER_NEWS_BASE_URL = "https://news.ycombinator.com/";
    private static final String STORY_ID = "story_id";
    private static final String TAG = StoryDetailFragment.class.getSimpleName();
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final int DISTANCE_TO_HIDE_ACTIONBAR = 1;
    @InjectView(R.id.prev_top_level) Button mPrevTopLevel;
    @InjectView(R.id.next_top_level) Button mNextTopLevel;
    @InjectView(R.id.web_back) Button mWebBack;
    @InjectView(R.id.close_link) Button mCloseLink;
    @InjectView(R.id.web_forward) Button mWebForward;
    @InjectView(R.id.swipe_container) SmoothSwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.comments_list) ListView mCommentsListView;
    @InjectView(R.id.open_link) Button mOpenLinkDialogButton;
    @InjectView(R.id.story_web_view) ObservableWebView mWebView;
    @InjectView(R.id.link_layout) ReboundRevealRelativeLayout mLinkLayout;
    @InjectView(R.id.fabbutton) FloatingActionButton mFloatingActionButton;
    private long mStoryId;
    private int mPrevVisibleItem;
    private HeaderViewHolder mHeaderViewHolder;
    private android.support.v7.app.ActionBar mActionBar;
    private CommentsListAdapter mListAdapter;
    private Bundle mWebViewBundle;
    private boolean mReadability;
    private StoryDetail mStoryDetail;

    public StoryDetailFragment() {
        // Required empty public constructor
    }

    public static StoryDetailFragment newInstance(long param1) {
        StoryDetailFragment fragment = new StoryDetailFragment();
        Bundle args = new Bundle();
        args.putLong(STORY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void expandComments(List<NodeHNAPIComment> expandedComments, NodeHNAPIComment nodeHNAPIComment) {
        expandedComments.add(nodeHNAPIComment);
        if (nodeHNAPIComment.getChildNodeHNAPIComments().size() == 0) {
            return;
        }

        for (NodeHNAPIComment childNodeHNAPIComment : nodeHNAPIComment.getChildNodeHNAPIComments()) {
            expandComments(expandedComments, childNodeHNAPIComment);
        }
    }

    private void refresh() {
        mSubscription = HoloHackerNewsApplication.getInstance().getHackerNewsServiceInstance().getItemDetails(mStoryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(nodeHNAPIStoryDetail -> {
                    List<NodeHNAPIComment> nodeHNAPIComments = nodeHNAPIStoryDetail.getNodeHNAPICommentList();
                    List<NodeHNAPIComment> expandedComments = new ArrayList<NodeHNAPIComment>();
                    for (NodeHNAPIComment nodeHNAPIComment : nodeHNAPIComments) {
                        expandComments(expandedComments, nodeHNAPIComment);
                    }

                    List<Comment> commentList = new ArrayList<Comment>();

                    for (NodeHNAPIComment expandedComment : expandedComments) {
                        Comment comment = new Comment(expandedComment.getId(), expandedComment.getLevel(),
                                expandedComment.getUser().toLowerCase().equals(nodeHNAPIStoryDetail.getUser().toLowerCase()),
                                expandedComment.getUser(), expandedComment.getTimeAgo(), expandedComment.getContent());
                        commentList.add(comment);
                    }

                    return new StoryDetail(nodeHNAPIStoryDetail.getId(), nodeHNAPIStoryDetail.getTitle(),
                            nodeHNAPIStoryDetail.getUrl(), nodeHNAPIStoryDetail.getDomain(),
                            nodeHNAPIStoryDetail.getPoints(), nodeHNAPIStoryDetail.getUser(),
                            nodeHNAPIStoryDetail.getTimeAgo(), nodeHNAPIStoryDetail.getCommentsCount(),
                            nodeHNAPIStoryDetail.getContent(), nodeHNAPIStoryDetail.getPoll(),
                            nodeHNAPIStoryDetail.getLink(), commentList, nodeHNAPIStoryDetail.getMoreCommentsId(),
                            nodeHNAPIStoryDetail.getType());
                })
                .subscribe(new Subscriber<StoryDetail>() {
                    @Override
                    public void onCompleted() {
                        showProgress(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                        Toast.makeText(getActivity(), "Problem loading page", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StoryDetail storyDetail) {
                        Log.d(TAG, storyDetail.toString());
                        mStoryDetail = storyDetail;
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
                                if (firstVisibleItem == 0) {
                                    mActionBar.setTitle("Hacker News");
                                }
                                else {
                                    mActionBar.setTitle(mStoryDetail.getTitle());
                                }
                            }
                        });

                        mOpenLinkDialogButton.setVisibility(View.VISIBLE);
                        mOpenLinkDialogButton.setOnClickListener(view -> {
                            mLinkLayout.setOpen(!mLinkLayout.isOpen());
                            if ("ask".equals(mStoryDetail.getType())) {
                                mStoryDetail.setUrl(HACKER_NEWS_BASE_URL + mStoryDetail.getUrl());
                            }
                            else if ("job".equals(mStoryDetail.getType())) {
                                if (mStoryDetail.getUrl().contains("/item?id=")) {
                                    mStoryDetail.setUrl(HACKER_NEWS_BASE_URL + mStoryDetail.getUrl());
                                }
                            }
                        });

                        mListAdapter.clear();
                        mListAdapter.addAll(mStoryDetail.getCommentList());

                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        View rootView = inflater.inflate(R.layout.fragment_story_comments, container, false);
        ButterKnife.inject(this, rootView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mContainer = rootView.findViewById(R.id.container);
        mFloatingActionButton.setOnClickListener(view -> readability());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mLinkLayout.setStashPixel(height);
        mLinkLayout.setRevealPixel(0);
        mLinkLayout.setTranslateDirection(ReboundRevealRelativeLayout.TRANSLATE_DIRECTION_VERTICAL);
        mLinkLayout.setOpen(false);

        final ProgressBar webProgressBar = (ProgressBar) mLinkLayout.findViewById(R.id.web_progress_bar);
        mCloseLink.setOnClickListener(view -> mLinkLayout.setOpen(false));

        mWebView.setVisibility(View.INVISIBLE);
        webProgressBar.setVisibility(View.VISIBLE);

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
                webProgressBar.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webProgressBar.setMax(100);
                webProgressBar.setProgress(newProgress);

            }
        });

        mWebView.setOnScrollChangedCallback(this);

        mWebBack.setOnClickListener(view -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
        });
        mWebForward.setOnClickListener(view -> {
            if (mWebView.canGoForward()) {
                mWebView.goForward();
            }
        });
        mPrevVisibleItem = 1;

        mActionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        if(mActionBar!=null) {
            mActionBar.show();
            mActionBar.setTitle("Hacker News");
        }

        showProgress(false);

        mCommentsListView = (ListView) rootView.findViewById(R.id.comments_list);

        mPrevTopLevel.setOnClickListener(view -> {
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
        });
        mNextTopLevel.setOnClickListener(view -> {
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
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
        });

        showProgress(true);
        refresh();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mWebViewBundle = savedInstanceState;
            mWebView.restoreState(mWebViewBundle);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebViewBundle = new Bundle();
        mWebView.saveState(mWebViewBundle);
    }

    @Override
    public void onDetach() {
        mSubscription.unsubscribe();
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
                builder.setItems(shareItems, (dialogInterface, i) -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    switch (i) {
                        case 0:
                            sendIntent.putExtra(Intent.EXTRA_TEXT, mStoryDetail.getUrl());
                            break;
                        case 1:
                            sendIntent.putExtra(Intent.EXTRA_TEXT, HACKER_NEWS_ITEM_BASE_URL + mStoryId);
                            break;
                    }
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
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

    public boolean isLinkViewVisible() {
        return mLinkLayout.isOpen();
    }

    public void hideLinkView() {
        mLinkLayout.setOpen(false);
    }

    private void readability() {
        mReadability = !mReadability;
        if (mReadability) {
            final String readabilityJS = "javascript:(%0A%28function%28%29%7Bwindow.baseUrl%3D%27//www.readability.com%27%3Bwindow.readabilityToken%3D%2798fX3vYgEcKF2uvS7HTuScqeDgegMF74HVHuLYwF%27%3Bvar%20s%3Ddocument.createElement%28%27script%27%29%3Bs.setAttribute%28%27type%27%2C%27text/javascript%27%29%3Bs.setAttribute%28%27charset%27%2C%27UTF-8%27%29%3Bs.setAttribute%28%27src%27%2CbaseUrl%2B%27/bookmarklet/read.js%27%29%3Bdocument.documentElement.appendChild%28s%29%3B%7D%29%28%29)";
            try {
                mWebView.loadUrl(URLDecoder.decode(readabilityJS, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            mWebView.loadUrl(mStoryDetail.getUrl());
        }
    }

    @Override
    public void onScroll(int l, int t, int oldL, int oldT) {
        if (t >= oldT) {
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
        @InjectView(R.id.job_content) TextView mContent;

        public HeaderViewHolder(View headerView) {
            ButterKnife.inject(this, headerView);
        }
    }
}
