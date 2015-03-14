package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.ui.ViewModelFragment;
import io.dwak.rx.events.RxEvents;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;

public class StoryListFragment extends ViewModelFragment<StoryListViewModel>{

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();
    public static final String TOP_OF_LIST = "TOP_OF_LIST";

    @InjectView(R.id.story_list) AbsListView mListView;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    private OnStoryListFragmentInteractionListener mListener;
    private StoryListAdapter mListAdapter;
    private List<Story> mStoryList;

    public static StoryListFragment newInstance(@StoryListViewModel.FeedType int param1) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putInt(FEED_TO_LOAD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void refresh() {
        mListAdapter.clear();
        react(getViewModel().getStories(), false);
    }

    private void react(Observable<Story> stories, boolean pageTwo) {
        stories.subscribe(new Subscriber<Story>() {
            @Override
            public void onCompleted() {
                showProgress(false);
                mSwipeRefreshLayout.setRefreshing(false);
                getViewModel().setPageTwoLoaded(pageTwo);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RetrofitError) {
                    Toast.makeText(getActivity(), "Unable to connect to API!", Toast.LENGTH_SHORT).show();
                }
                else {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNext(Story story) {
                if (story.getStoryId() != null && mListAdapter.getPosition(story) == -1) {
                    mListAdapter.add(story);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStoryListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStoryListFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            @StoryListViewModel.FeedType final int feedType = getArguments().getInt(FEED_TO_LOAD);
            getViewModel().setFeedType(feedType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getRootView(inflater, container);
        ButterKnife.inject(this, view);

        getViewModel().setPageTwoLoaded(false);

        mContainer = view.findViewById(R.id.story_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getViewModel().getTitle());
        }
        showProgress(true);

        if (savedInstanceState == null || mListAdapter == null || mStoryList == null) {
            // Set the adapter
            mStoryList = new ArrayList<>();
            mListAdapter = new StoryListAdapter(getActivity(),
                    UserPreferenceManager.isNightModeEnabled(getActivity())
                            ? R.layout.comments_header_dark
                            : R.layout.comments_header,
                    mStoryList);

            if (!getViewModel().isReturningUser()) {
                getViewModel().getBetaAlert(getActivity())
                        .show();

                getViewModel().setReturningUser(true);
            }

            refresh();
        }
        else {
            showProgress(false);
        }

        RxEvents.observableFromListItemClick(mListView)
                .subscribe(rxListItemClickEvent -> {
                    if (mListener != null) {
                        mListener.onStoryListFragmentInteraction(mListAdapter.getItemId(rxListItemClickEvent.getPosition()), rxListItemClickEvent.getView());
                    }
                });
        if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_TOP) {
            mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (!getViewModel().isPageTwoLoaded()) {
                        react(getViewModel().getTopStoriesPageTwo(), true);
                    }
                }
            });
        }
        mListView.setAdapter(mListAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getViewModel().getColorSchemeColors());
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
        });

        if(savedInstanceState!=null){
            mListView.setSelection(savedInstanceState.getInt(TOP_OF_LIST));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOP_OF_LIST, mListView.getFirstVisiblePosition());
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(UserPreferenceManager.isNightModeEnabled(getActivity())
                        ? R.layout.fragment_storylist_list_dark
                        : R.layout.fragment_storylist_list,
                container,
                false);
    }

    @Override
    protected Class<StoryListViewModel> getViewModelClass() {
        return StoryListViewModel.class;
    }

    public interface OnStoryListFragmentInteractionListener {
        void onStoryListFragmentInteraction(long id, View view);
    }
}
