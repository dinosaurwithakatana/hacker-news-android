package io.dwak.holohackernews.app.ui.storylist;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.ui.BaseFragment;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.manager.FeedType;
import io.dwak.holohackernews.app.manager.HackerNewsCallback;
import io.dwak.holohackernews.app.manager.HackerNewsException;
import io.dwak.holohackernews.app.manager.HackerNewsManager;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.widget.SmoothSwipeRefreshLayout;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link StoryListFragment.OnStoryListFragmentInteractionListener}
 * interface.
 */
public class StoryListFragment extends BaseFragment implements AbsListView.OnItemClickListener {

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();
    /**
     * The fragment's ListView/GridView.
     */
    @InjectView(R.id.story_list) AbsListView mListView;
    private String mTitle;
    private FeedType mFeedType;
    private List<Story> mStoryList;
    private OnStoryListFragmentInteractionListener mListener;
    private SmoothSwipeRefreshLayout mSwipeRefreshLayout;
    private StoryListAdapter mListAdapter;
    private HackerNewsManager mHackerNewsManager;
    private boolean mPageTwoLoaded;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoryListFragment() {
    }

    public static StoryListFragment newInstance(FeedType param1) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putSerializable(FEED_TO_LOAD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFeedType = (FeedType) getArguments().getSerializable(FEED_TO_LOAD);
        }

        mHackerNewsManager = HackerNewsManager.getInstance();
        mStoryList = new ArrayList<Story>();
        mPageTwoLoaded = false;
    }

    private void refresh() {
        mHackerNewsManager.getStories(mFeedType, new StoryRequestCallback());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storylist_list, container, false);
        ButterKnife.inject(this, view);

        mContainer = view.findViewById(R.id.story_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
        switch (mFeedType) {
            case TOP:
                mTitle = "Top";
                break;
            case BEST:
                mTitle = "Best";
                break;
            case NEW:
                mTitle = "Newest";
                break;
        }
        actionBar.setTitle(mTitle);
        showProgress(true);

        // Set the adapter
        mStoryList = new ArrayList<Story>();
        mListAdapter = new StoryListAdapter(mStoryList, getActivity(), R.layout.comments_header);

        // Assign the ListView to the AnimationAdapter and vice versa
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mListAdapter);
        scaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(scaleInAnimationAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        if (mFeedType == FeedType.TOP) {
            mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (!mPageTwoLoaded) {
                        mHackerNewsManager.getTopStoriesPageTwo(new HackerNewsCallback<List<Story>>() {
                            @Override
                            public void onResponse(List<Story> response, HackerNewsException exception) {
                                if (exception == null) {
                                    mListAdapter.addAll(response);
                                    mPageTwoLoaded = true;
                                }
                            }
                        });
                    }
                }
            });
        }

        refresh();

        mSwipeRefreshLayout = (SmoothSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onStoryListFragmentInteraction(mListAdapter.getItemId(position));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStoryListFragmentInteractionListener {
        public void onStoryListFragmentInteraction(long id);
    }

    private class StoryRequestCallback implements HackerNewsCallback<List<Story>> {
        @Override
        public void onResponse(List<Story> response, HackerNewsException exception) {
            if (exception == null) {
                mListAdapter.addAll(response);
                showProgress(false);
                mSwipeRefreshLayout.setRefreshing(false);
            }
            else {
                exception.printStackTrace();
            }
        }
    }
}
