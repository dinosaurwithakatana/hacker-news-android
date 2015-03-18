package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;

public class StoryListFragment extends ViewModelFragment<StoryListViewModel>{

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();
    public static final String TOP_OF_LIST = "TOP_OF_LIST";

    @InjectView(R.id.story_list) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    private OnStoryListFragmentInteractionListener mListener;
    private List<Story> mStoryList;
    private StoryRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;

    public static StoryListFragment newInstance(@StoryListViewModel.FeedType int param1) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putInt(FEED_TO_LOAD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void refresh() {
        mRecyclerAdapter.clear();
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
                if (story.getStoryId() != null
                        && mRecyclerAdapter.getPositionOfItem(story) == -1) {
                    mRecyclerAdapter.addStory(story);
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

        if (savedInstanceState == null || mRecyclerAdapter == null || mStoryList == null) {
            // Set the adapter
            mStoryList = new ArrayList<>();
            mRecyclerAdapter = new StoryRecyclerAdapter(getActivity(),
                    new ArrayList<>(),
                    UserPreferenceManager.isNightModeEnabled(getActivity()) ? R.layout.comments_header_dark : R.layout.comments_header,
                    position -> mListener.onStoryListFragmentInteraction(mRecyclerAdapter.getItemId(position)));
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));

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

        if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_TOP) {
            mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (!getViewModel().isPageTwoLoaded()) {
                        react(getViewModel().getTopStoriesPageTwo(), true);
                    }
                }
            });
        }

        mRecyclerView.setAdapter(mRecyclerAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
        });

        if(savedInstanceState!=null){
            mLayoutManager.scrollToPosition(savedInstanceState.getInt(TOP_OF_LIST));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOP_OF_LIST, mLayoutManager.findFirstVisibleItemPosition());
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
        void onStoryListFragmentInteraction(long id);
    }
}
