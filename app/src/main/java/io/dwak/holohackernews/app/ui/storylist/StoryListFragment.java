package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.manager.hackernews.FeedType;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.ui.BaseFragment;
import io.dwak.holohackernews.app.widget.SmoothSwipeRefreshLayout;
import io.dwak.rx.events.RxEvents;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link StoryListFragment.OnStoryListFragmentInteractionListener}
 * interface.
 */
public class StoryListFragment extends BaseFragment {

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();

    @InjectView(R.id.story_list) AbsListView mListView;
    @InjectView(R.id.swipe_container) SmoothSwipeRefreshLayout mSwipeRefreshLayout;

    private String mTitle;
    private FeedType mFeedType;
    private OnStoryListFragmentInteractionListener mListener;
    private StoryListAdapter mListAdapter;
    private boolean mPageTwoLoaded;
    private HackerNewsService mHackerNewsService;

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

    private void refresh() {
        mListAdapter.clear();
        Observable<List<NodeHNAPIStory>> observable = null;
        switch (mFeedType) {
            case TOP:
                observable = mHackerNewsService.getTopStories();
                break;
            case BEST:
                observable = mHackerNewsService.getBestStories();
                break;
            case NEW:
                observable = mHackerNewsService.getNewestStories();
                break;
        }

        react(observable, false);
    }

    private void react(Observable<List<NodeHNAPIStory>> observable, boolean pageTwo) {
        mSubscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(nodeHNAPIStories -> Observable.from(nodeHNAPIStories))
                .map(nodeStory -> new Story(nodeStory.getStoryId(),
                        nodeStory.getTitle(),
                        nodeStory.getUrl(),
                        nodeStory.getDomain(),
                        nodeStory.getPoints(),
                        nodeStory.getSubmitter(),
                        nodeStory.getPublishedTime(),
                        nodeStory.getNumComments(),
                        nodeStory.getType()))
                .subscribe(new Subscriber<Story>() {
                    @Override
                    public void onCompleted() {
                        showProgress(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mPageTwoLoaded = pageTwo;
                    }

                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(getActivity())
                                .setPositiveButton("Restart", (dialogInterface, i) -> {
                                    Intent mStartActivity = new Intent(getActivity(), MainActivity.class);
                                    int mPendingIntentId = 123456;
                                    PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                    getActivity().finish();
                                })
                                .setNegativeButton("Close app", (dialogInterface, i) -> getActivity().finish())
                                .setMessage("Something went wrong!")
                                .create()
                                .show();
                    }

                    @Override
                    public void onNext(Story story) {
                        mListAdapter.add(story);
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
            mFeedType = (FeedType) getArguments().getSerializable(FEED_TO_LOAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storylist_list, container, false);
        ButterKnife.inject(this, view);

        mHackerNewsService = HoloHackerNewsApplication.getInstance().getHackerNewsServiceInstance();
        List<Story> storyList;
        mPageTwoLoaded = false;

        mContainer = view.findViewById(R.id.story_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
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
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
        showProgress(true);

        // Set the adapter
        storyList = new ArrayList<>();
        mListAdapter = new StoryListAdapter(storyList, getActivity(), R.layout.comments_header);

        // Assign the ListView to the AnimationAdapter and vice versa
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mListAdapter);
        scaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(scaleInAnimationAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        RxEvents.observableFromListItemClick(mListView)
                .subscribe(rxListItemClickEvent -> {
                    if (mListener != null) {
                        mListener.onStoryListFragmentInteraction(mListAdapter.getItemId(rxListItemClickEvent.getPosition()));
                    }
                });
        if (mFeedType == FeedType.TOP) {
            mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (!mPageTwoLoaded) {
                        Observable<List<NodeHNAPIStory>> observable = mHackerNewsService.getTopStoriesPageTwo();
                        react(observable, true);
                    }
                }
            });
        }

        refresh();

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
        });

        return view;
    }

    @Override
    public void onDetach() {
        mListener = null;
        mSubscription.unsubscribe();
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStoryListFragmentInteractionListener {
        public void onStoryListFragmentInteraction(long id);
    }
}
