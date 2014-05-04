package com.dwak.holohackernews.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import com.dwak.holohackernews.app.network.HackerNewsService;
import com.dwak.holohackernews.app.network.models.Story;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardListView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

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
    private FeedType mFeedType;
    private List<Story> mStoryList;
    private StoryListAdapter mListAdapter;
    private ArrayList<Card> mCards;
    private OnStoryListFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * The fragment's ListView/GridView.
     */
    private CardListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private CardArrayAdapter mCardArrayAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoryListFragment() {
    }

    // TODO: Rename and change types of parameters
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

        mStoryList = new ArrayList<Story>();
        mCards = new ArrayList<Card>();
        mListAdapter = new StoryListAdapter(getActivity(), R.layout.story_item, mStoryList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storylist, container, false);

        mContainer = view.findViewById(R.id.story_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        showProgress(true);

        // Set the adapter
        mListView = (CardListView) view.findViewById(R.id.story_list);
//        mListView.setAdapter(mListAdapter);
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), mCards);

        mListView.setAdapter(mCardArrayAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int mPrevVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                if (mPrevVisibleItem != firstVisibleItem) {
                    if (mPrevVisibleItem < firstVisibleItem)
                        getActivity().getActionBar().hide();
                    else
                        getActivity().getActionBar().show();

                    mPrevVisibleItem = firstVisibleItem;
                }
            }
        });

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        refresh();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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

        return view;
    }

    private void refresh() {
        switch (mFeedType) {
            case TOP:
                mService.getTopStories(new StoryRequestCallback());
                break;
            case BEST:
                mService.getBestStories(new StoryRequestCallback());
                break;
            case NEW:
                mService.getNewestStories(new StoryRequestCallback());
                break;
        }
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

    public static enum FeedType {
        TOP,
        BEST,
        NEW
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
        // TODO: Update argument type and name
        public void onStoryListFragmentInteraction(long id);
    }

    class StoryRequestCallback implements Callback<List<Story>> {

        @Override
        public void success(List<Story> stories, Response response) {
            mStoryList = stories;
            mListAdapter.setStories(mStoryList);
            mListAdapter.notifyDataSetChanged();

            for (final Story story : mStoryList) {
                Card newCard = new Card(getActivity());
                newCard.setTitle(story.getPoints() + " points | " + story.getSubmitter() + " | " + story.getDomain());

                CardHeader header = new CardHeader(getActivity());
                header.setTitle(story.getTitle().length() > 50 ? story.getTitle().substring(0, 50) + "..." : story.getTitle());
                newCard.addCardHeader(header);
                newCard.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        mListener.onStoryListFragmentInteraction(story.getStoryId());
                    }
                });
                newCard.setOnLongClickListener(new Card.OnLongCardClickListener() {
                    @Override
                    public boolean onLongClick(Card card, View view) {
                        return false;
                    }
                });
                newCard.setBackgroundResourceId(R.drawable.card_selector);
                mCards.add(newCard);
            }

            showProgress(false);
            mSwipeRefreshLayout.setRefreshing(false);
            mCardArrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, error.getLocalizedMessage());
        }
    }

}
