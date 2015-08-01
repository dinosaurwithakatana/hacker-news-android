package io.dwak.holohackernews.app.ui.storydetail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelFragment;
import io.dwak.holohackernews.app.dagger.component.DaggerViewModelComponent;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.util.HNLog;
import io.dwak.holohackernews.app.util.UIUtils;
import io.dwak.holohackernews.app.widget.ObservableWebView;
import rx.Subscriber;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoryDetailFragment extends BaseViewModelFragment<StoryDetailViewModel> implements ObservableWebView.OnScrollChangedCallback {
    public static final String HACKER_NEWS_ITEM_BASE_URL = "https://news.ycombinator.com/item?id=";
    public static final String HACKER_NEWS_BASE_URL = "https://news.ycombinator.com/";
    public static final String LINK_DRAWER_OPEN = "LINK_DRAWER_OPEN";
    public static final String TOP_VISIBLE_COMMENT = "TOP_VISIBLE_COMMENT";
    public static final String LOADING_FROM_SAVED = "LOADING_FROM_SAVED";
    private static final String STORY_ID = "story_id";
    private static final String TAG = StoryDetailFragment.class.getSimpleName();
    @InjectView(R.id.button_bar) RelativeLayout mButtonBar;
    @InjectView(R.id.action_1) Button mButtonBarAction1;
    @InjectView(R.id.action_main) Button mButtonBarMainAction;
    @InjectView(R.id.action_2) Button mButtonBarAction2;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.comments_recycler) RecyclerView mCommentsRecyclerView;
    @InjectView(R.id.story_web_view) ObservableWebView mWebView;
    @InjectView(R.id.link_layout) RelativeLayout mLinkLayout;
    @InjectView(R.id.fabbutton) FloatingActionButton mFloatingActionButton;
    @InjectView(R.id.saved_banner) TextView mSavedBanner;
    @InjectView(R.id.link_panel) SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.web_progress_bar) ProgressBar mWebProgressBar;
    @Inject StoryDetailViewModel mViewModel;
    private Bundle mWebViewBundle;
    private SlidingUpPanelLayout.PanelState mOldPanelState;
    private StoryDetailRecyclerAdapter mAdapter;
    private int mCurrentFirstCompletelyVisibleItemIndex = 0;
    private LinearLayoutManager mLayoutManager;

    public static StoryDetailFragment newInstance(long id, boolean saved) {
        StoryDetailFragment fragment = StoryDetailFragment.newInstance(id);
        Bundle args = fragment.getArguments();
        args.putBoolean(LOADING_FROM_SAVED, saved);
        fragment.setArguments(args);
        return fragment;
    }

    public static StoryDetailFragment newInstance(long param1) {
        StoryDetailFragment fragment = new StoryDetailFragment();
        Bundle args = new Bundle();
        args.putLong(STORY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void refresh() {
        showProgress(true);
        mSubscription = AndroidObservable.bindFragment(this, getViewModel().getStoryDetailObservable())
                                         .subscribeOn(Schedulers.io())
                                         .observeOn(AndroidSchedulers.mainThread())
                                         .subscribe(new Subscriber<StoryDetail>() {
                                             @Override
                                             public void onCompleted() {
                                                 showProgress(false);
                                                 mSwipeRefreshLayout.setRefreshing(false);
                                             }

                                             @Override
                                             public void onError(Throwable e) {
                                                 Toast.makeText(getActivity(), R.string.story_details_error_toast_message, Toast.LENGTH_SHORT).show();
                                                 if (HackerNewsApplication.isDebug()) {
                                                     e.printStackTrace();
                                                 }
                                             }

                                             @Override
                                             public void onNext(StoryDetail storyDetail) {
                                                 updateHeader(storyDetail);
                                                 updateSlidingPanel(getViewModel().startDrawerExpanded());
                                                 updateRecyclerView(storyDetail);
                                                 openLink(storyDetail);
                                             }
                                         });
    }

    private void updateRecyclerView(StoryDetail storyDetail) {
        mAdapter.clear();
        for (Comment comment : storyDetail.getCommentList()) {
            mAdapter.addComment(comment);
        }
    }

    private void updateHeader(StoryDetail storyDetail) {
        mAdapter.updateHeader(storyDetail);
    }

    private void openLink(StoryDetail storyDetail) {
        if (UserPreferenceManager.getInstance().showLinkFirst() && UserPreferenceManager.getInstance().isExternalBrowserEnabled()) {
            openLinkInExternalBrowser();
        }
        else {
            if (mWebViewBundle == null && !UserPreferenceManager.getInstance().isExternalBrowserEnabled()) {
                mWebView.loadUrl(storyDetail.getUrl());
            }
            else {
                mWebView.restoreState(mWebViewBundle);
            }
        }
    }

    private void openLinkInExternalBrowser() {
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(getViewModel().getStoryDetail().getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerViewModelComponent.builder()
                                .appComponent(HackerNewsApplication.getAppComponent())
                                .build()
                                .inject(this);

        if (getArguments() != null) {
            if (getArguments().containsKey(STORY_ID)) {
                long storyId = getArguments().getLong(STORY_ID);
                getViewModel().setStoryId(storyId);
            }

            if (getArguments().containsKey(LOADING_FROM_SAVED)) {
                getViewModel().setLoadFromSaved(getArguments().getBoolean(LOADING_FROM_SAVED));
            }
        }
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_comments, container, false);
        if (savedInstanceState != null) {
            mOldPanelState = (SlidingUpPanelLayout.PanelState) savedInstanceState.getSerializable(LINK_DRAWER_OPEN);
        }
        ButterKnife.inject(this, rootView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mContainer = rootView.findViewById(R.id.container);
        mFloatingActionButton.setOnClickListener(view -> readability());
        mSavedBanner.setVisibility(getViewModel().isSaved() ? View.VISIBLE : View.GONE);
        setupWebViewDrawer();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(getString(R.string.app_name));
        }


        mAdapter = new StoryDetailRecyclerAdapter(getActivity(), new StoryDetailRecyclerAdapter.StoryDetailRecyclerListener() {
            @Override
            public void onCommentClicked(int position) {
                if (mAdapter.areChildrenHidden(position)) {
                    mAdapter.showChildComments(position);
                }
                else {
                    mAdapter.hideChildComments(position);
                }
            }

            @Override
            public void onCommentActionClicked(Comment comment) {
                final CharSequence[] commentActions = getResources().getStringArray(getViewModel().getCommentActions());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(commentActions, (dialogInterface, j) -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    switch (j) {
                        case 0:
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                                String.format("https://news.ycombinator.com/item?id=%d", comment.getCommentId()));
                            break;
                        case 1:
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                                String.format("%s: %s", comment.getUser(), Html.fromHtml(comment.getContent())));
                            break;
                        case 2:
                            AlertDialog.Builder replyDialog = new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.action_reply));
                            AppCompatEditText editText = new AppCompatEditText(getActivity());
                            replyDialog.setView(editText)
                                       .setPositiveButton(getString(R.string.action_submit), (dialog, which) -> {
                                           ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                           progressDialog.setMessage(getString(R.string.submitting_progress));
                                           progressDialog.setCancelable(false);
                                           progressDialog.show();
                                           getViewModel().reply(comment, editText.getText().toString())
                                                         .subscribeOn(Schedulers.io())
                                                         .observeOn(AndroidSchedulers.mainThread())
                                                         .subscribe(new Subscriber<Object>() {
                                                             @Override
                                                             public void onCompleted() {
                                                                 progressDialog.dismiss();
                                                             }

                                                             @Override
                                                             public void onError(Throwable e) {
                                                                 progressDialog.dismiss();
                                                             }

                                                             @Override
                                                             public void onNext(Object o) {

                                                             }
                                                         });
                                           dialog.dismiss();
                                       })
                                       .setNegativeButton(android.R.string.cancel, null)
                                       .show();
                            dialogInterface.dismiss();
                            return;
                        case 3:
                            getViewModel().upvote(comment)
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Subscriber<Object>() {
                                              @Override
                                              public void onCompleted() {

                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {

                                              }
                                          });
                            return;
                    }
                    sendIntent.setType("text/plain");
                    getActivity().startActivity(sendIntent);
                });

                builder.create().show();
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mCommentsRecyclerView.setLayoutManager(mLayoutManager);
        mCommentsRecyclerView.setAdapter(mAdapter);
        mCommentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentFirstCompletelyVisibleItemIndex = mLayoutManager.findFirstVisibleItemPosition();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                                                    android.R.color.holo_orange_light,
                                                    android.R.color.holo_orange_dark,
                                                    android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
        });

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
        outState.putSerializable(LINK_DRAWER_OPEN, mSlidingUpPanelLayout.getPanelState());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
        inflater.inflate(R.menu.menu_story_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                final CharSequence[] shareItems = {getString(R.string.action_share_link), getString(R.string.action_share_comments)};
                new AlertDialog.Builder(getActivity())
                        .setItems(shareItems, (dialogInterface, i) -> {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            switch (i) {
                                case 0:
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, getViewModel().getStoryDetail().getUrl());
                                    break;
                                case 1:
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, HACKER_NEWS_ITEM_BASE_URL + getViewModel().getStoryId());
                                    break;
                            }
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        })
                        .create()
                        .show();
                break;
            case R.id.action_open_browser:
                final CharSequence[] openInBrowserItems = {getString(R.string.action_open_in_browser_link),
                        getString(R.string.action_open_in_browser_comments)};
                new AlertDialog.Builder(getActivity())
                        .setItems(openInBrowserItems, (dialogInterface, i) -> {
                            Intent browserIntent = new Intent();
                            browserIntent.setAction(Intent.ACTION_VIEW);
                            switch (i){
                                case 0:
                                    browserIntent.setData(Uri.parse(getViewModel().getStoryDetail().getUrl()));
                                    break;
                                case 1:
                                    browserIntent.setData(Uri.parse(HACKER_NEWS_ITEM_BASE_URL + getViewModel().getStoryId()));
                                    break;
                            }
                            startActivity(browserIntent);
                        })
                        .create()
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected StoryDetailViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebViewBundle = new Bundle();
        mWebView.saveState(mWebViewBundle);
    }

    private void updateSlidingPanel(boolean expanded) {
        if (getViewModel().useExternalBrowser()) {
            mSlidingUpPanelLayout.setTouchEnabled(false);
        }
        mButtonBarMainAction.setOnClickListener(v -> {
            if (getViewModel().useExternalBrowser()) {
                openLinkInExternalBrowser();
            }
            else {
                mSlidingUpPanelLayout.setPanelState(mSlidingUpPanelLayout.getPanelState()
                                                                         .equals(SlidingUpPanelLayout.PanelState.COLLAPSED) ? SlidingUpPanelLayout.PanelState.EXPANDED
                                                                                                                            : SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mSlidingUpPanelLayout.post(() -> {

            if (expanded) {
                mButtonBarMainAction.setText(getString(R.string.show_comments));
                mButtonBarAction1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
                mButtonBarAction1.setOnClickListener(view -> {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                });
                mButtonBarAction2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward));
                mButtonBarAction2.setOnClickListener(view -> {
                    if (mWebView.canGoForward()) {
                        mWebView.goForward();
                    }
                });
            }
            else {
                mButtonBarMainAction.setText(getString(R.string.show_link));
                mButtonBarAction1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up));
                mButtonBarAction1.setOnClickListener(view -> {
                    for (int i = mCurrentFirstCompletelyVisibleItemIndex - 1; i >= 0; i--) {
                        final Object item = mAdapter.getItem(i);
                        if (item instanceof Comment && ((Comment) item).getLevel() == 0) {
                            HNLog.d(TAG, String.valueOf(i));
                            mCurrentFirstCompletelyVisibleItemIndex = i;
                            mLayoutManager.scrollToPositionWithOffset(i, 0);
                            if(HackerNewsApplication.isDebug()) UIUtils.showToast(getActivity(), String.valueOf(i));
                            return;
                        }
                    }
                });
                mButtonBarAction2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down));
                mButtonBarAction2.setOnClickListener(view -> {
                    for (int i = mCurrentFirstCompletelyVisibleItemIndex + 1; i < mAdapter.getItemCount(); i++) {
                        final Object item = mAdapter.getItem(i);
                        if (item instanceof Comment && ((Comment) item).getLevel() == 0) {
                            HNLog.d(TAG, String.valueOf(i));
                            mCurrentFirstCompletelyVisibleItemIndex = i;
                            mLayoutManager.scrollToPositionWithOffset(i, 0);
                            if(HackerNewsApplication.isDebug()) UIUtils.showToast(getActivity(), String.valueOf(i));
                            return;
                        }
                    }
                });

                mButtonBarAction1.setVisibility(View.VISIBLE);
                mButtonBarAction2.setVisibility(View.VISIBLE);
            }
        });

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebViewDrawer() {
        mSlidingUpPanelLayout.setDragView(mButtonBar);
        mSlidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View panelView) {
                updateSlidingPanel(false);
            }

            @Override
            public void onPanelExpanded(View panelView) {
                updateSlidingPanel(true);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
        if (!UserPreferenceManager.getInstance().isExternalBrowserEnabled()) {
            if (mOldPanelState == SlidingUpPanelLayout.PanelState.EXPANDED || (UserPreferenceManager.getInstance().showLinkFirst())) {
                mButtonBarMainAction.setText(getResources().getString(R.string.show_comments));
                mSlidingUpPanelLayout.postDelayed(() -> mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED), getResources().getInteger(R.integer.fragment_animation_times));
            }
            else {
                mButtonBarMainAction.setText(getResources().getString(R.string.show_link));
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }


        mWebProgressBar.setVisibility(View.VISIBLE);
        mWebProgressBar.setMax(100);

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
                if (mWebProgressBar != null) {
                    mWebProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mWebProgressBar != null) {
                    if (mWebProgressBar.getVisibility() == View.GONE) {
                        mWebProgressBar.setVisibility(View.VISIBLE);
                    }
                    mWebProgressBar.setProgress(newProgress);
                }

            }
        });

        mWebView.setOnScrollChangedCallback(this);
    }

    public boolean isLinkViewVisible() {
        return mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    public void hideLinkView() {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void readability() {
        getViewModel().setIsViewingReadability(!getViewModel().isViewingReadability());
        if (getViewModel().isViewingReadability()) {
            if (getViewModel().getReadabilityUrl() != null) {
                mWebView.loadUrl(getViewModel().getReadabilityUrl());
            }
        }
        else {
            mWebView.loadUrl(getViewModel().getStoryDetail().getUrl());
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
}
