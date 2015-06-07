package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelFragment;
import io.dwak.holohackernews.app.databinding.StoryListFragmentBinder;
import io.dwak.holohackernews.app.models.Story;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;

public class StoryListFragment extends BaseViewModelFragment<StoryListViewModel> {

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();
    public static final String TOP_OF_LIST = "TOP_OF_LIST";

    private OnStoryListFragmentInteractionListener mListener;
    private StoryRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private StoryListFragmentBinder mBinder;

    public static StoryListFragment newInstance(@StoryListViewModel.FeedType int feedType) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putInt(FEED_TO_LOAD, feedType);
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
                mBinder.swipeContainer.setRefreshing(false);
                getViewModel().setPageTwoLoaded(pageTwo);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RetrofitError) {
                    Toast.makeText(getActivity(), "Unable to connect to API!", Toast.LENGTH_SHORT).show();
                } else {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNext(Story story) {
                if (story.getStoryId() != null && mRecyclerAdapter.getPositionOfItem(story) == -1) {
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
        if (getArguments() != null) {
            @StoryListViewModel.FeedType final int feedType = getArguments().getInt(FEED_TO_LOAD);
            getViewModel().setFeedType(feedType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_storylist_list, container, false);
        View view = mBinder.getRoot();

        getViewModel().setPageTwoLoaded(false);

        mContainer = mBinder.recycler;
        mProgressBar = mBinder.progressBar;

        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getViewModel().getTitle());
        }
        showProgress(true);

        if (savedInstanceState == null || mRecyclerAdapter == null) {
            // Set the adapter
            mRecyclerAdapter = new StoryRecyclerAdapter(getActivity(),
                    new ArrayList<>(),
                    position -> mListener.onStoryListFragmentInteraction(mRecyclerAdapter.getItemId(position)));
            mLayoutManager = new LinearLayoutManager(getActivity());
            mBinder.recycler.setLayoutManager(mLayoutManager);
            mBinder.recycler.addItemDecoration(new SpacesItemDecoration(8));

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
            mBinder.recycler.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (!getViewModel().isPageTwoLoaded()) {
                        react(getViewModel().getTopStoriesPageTwo(), true);
                    }
                }
            });
        }

        mBinder.recycler.setAdapter(mRecyclerAdapter);

        mBinder.swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));
        mBinder.swipeContainer.setOnRefreshListener(() -> {
            mBinder.swipeContainer.setRefreshing(true);
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
        return inflater.inflate(R.layout.fragment_storylist_list, container, false);
    }

    @Override
    protected Class<StoryListViewModel> getViewModelClass() {
        return StoryListViewModel.class;
    }

    public interface OnStoryListFragmentInteractionListener {
        void onStoryListFragmentInteraction(long id);
    }
}
