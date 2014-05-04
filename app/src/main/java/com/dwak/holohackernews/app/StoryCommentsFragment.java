package com.dwak.holohackernews.app;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.dwak.holohackernews.app.network.models.Comment;
import com.dwak.holohackernews.app.network.models.StoryDetail;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoryCommentsFragment.OnStoryFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryCommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryCommentsFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STORY_ID = "story_id";
    private static final String TAG = StoryCommentsFragment.class.getSimpleName();

    private long mStoryId;
    private List<Comment> mCommentList;
    private CommentsListAdapter mListAdapter;

    private OnStoryFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mCommentsListView;
    private ActionBar mActionBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StoryCommentsFragment.
     */
    public static StoryCommentsFragment newInstance(long param1) {
        StoryCommentsFragment fragment = new StoryCommentsFragment();
        Bundle args = new Bundle();
        args.putLong(STORY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public StoryCommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoryId = getArguments().getLong(STORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story, container, false);


        mActionBar = getActivity().getActionBar();
        mActionBar.show();
        mActionBar.setTitle("Hacker News");
        mCommentList = new ArrayList<Comment>();
        mCommentsListView = (ListView) rootView.findViewById(R.id.comments_list);

        mCommentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    final View actionLayout = view.findViewById(R.id.comment_item_action_layout);
                    actionLayout.setVisibility(actionLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }

                return false;
            }
        });
        mListAdapter = new CommentsListAdapter(getActivity(), R.layout.comments_list_item, mCommentList);
        mCommentsListView.setAdapter(mListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
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
        mService.getItemDetails(mStoryId, new Callback<StoryDetail>() {
            @Override
            public void success(final StoryDetail storyDetail, Response response) {
                Toast.makeText(getActivity(), storyDetail.getTitle(), Toast.LENGTH_SHORT).show();
                mCommentsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    public int mPrevVisibleItem;

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                        if (mPrevVisibleItem != firstVisibleItem) {
                            if (mPrevVisibleItem < firstVisibleItem)
                                mActionBar.hide();
                            else
                                mActionBar.show();

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
                mListAdapter.setStoryDetail(storyDetail);
                mListAdapter.setComments(storyDetail.getCommentList());
                mListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onStoryFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnStoryFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onStoryFragmentInteraction(Uri uri);
    }

}
