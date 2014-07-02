package io.dwak.holohackernews.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;
import io.dwak.holohackernews.app.network.models.Comment;
import io.dwak.holohackernews.app.network.models.StoryDetail;
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

    private final int DISTANCE_TO_HIDE_ACTIONBAR = 1;

    private long mStoryId;
    private List<Comment> mCommentList;
    private CommentsListAdapter mListAdapter;

    private StoryDetail mStoryDetail;

    private OnStoryFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mCommentsListView;
    private ActionBar mActionBar;

    private Button mPreviousTopLevelButton;
    private Button mNextTopLevelButton;
    private Button mOpenLinkDialogButton;

    private HeaderViewHolder mHeaderViewHolder;
    private int mPrevVisibleItem;

    public StoryCommentsFragment() {
        // Required empty public constructor
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoryId = getArguments().getLong(STORY_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story_comments, container, false);

        mPrevVisibleItem = 1;

        mActionBar = getActivity().getActionBar();
        mActionBar.show();
        mActionBar.setTitle("Hacker News");
        mCommentList = new ArrayList<Comment>();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mContainer = rootView.findViewById(R.id.container);
        showProgress(true);

        mCommentsListView = (ListView) rootView.findViewById(R.id.comments_list);

        mPreviousTopLevelButton = (Button) rootView.findViewById(R.id.prev_top_level);
        mPreviousTopLevelButton.setOnClickListener(new View.OnClickListener() {
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
        mNextTopLevelButton = (Button) rootView.findViewById(R.id.next_top_level);
        mNextTopLevelButton.setOnClickListener(new View.OnClickListener() {
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
        mListAdapter = new CommentsListAdapter(getActivity(), R.layout.comments_list_item, mCommentList);
        mHeaderViewHolder = new HeaderViewHolder();
        View headerView = inflater.inflate(R.layout.comments_header, null);
        mHeaderViewHolder.mStoryTitle = (TextView) headerView.findViewById(R.id.story_title);
        mHeaderViewHolder.mStoryDomain = (TextView) headerView.findViewById(R.id.story_domain);
        mHeaderViewHolder.mStorySubmitter = (TextView) headerView.findViewById(R.id.story_submitter);
        mHeaderViewHolder.mStoryPoints = (TextView) headerView.findViewById(R.id.story_points);
        mHeaderViewHolder.mStoryLongAgo = (TextView) headerView.findViewById(R.id.story_long_ago);
        mHeaderViewHolder.mCommentsCount = (TextView) headerView.findViewById(R.id.comment_count);
        mHeaderViewHolder.mContent = (TextView) headerView.findViewById(R.id.job_content);

        mCommentsListView.setHeaderDividersEnabled(false);
        mCommentsListView.addHeaderView(headerView);
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

        mOpenLinkDialogButton = (Button) rootView.findViewById(R.id.open_link);
        refresh();
        return rootView;
    }

    private void refresh() {
        mHackerNewsService.getItemDetails(mStoryId, new Callback<StoryDetail>() {

            @Override
            public void success(final StoryDetail storyDetail, Response response) {
                mStoryDetail = storyDetail;
                mHeaderViewHolder.mStoryTitle.setText(storyDetail.getTitle());
                mHeaderViewHolder.mStorySubmitter.setText(storyDetail.getUser());
                if (!"job".equals(storyDetail.getType())) {
                    mHeaderViewHolder.mContent.setVisibility(View.GONE);
                    if ("link".equals(storyDetail.getType())) {
                        String domain = storyDetail.getDomain();
                        mHeaderViewHolder.mStoryDomain.setVisibility(View.VISIBLE);
                        mHeaderViewHolder.mStoryDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20));
                    }
                    else if ("ask".equals(storyDetail.getType())) {
                        mHeaderViewHolder.mStoryDomain.setVisibility(View.GONE);

                        mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                        Spanned jobContent = Html.fromHtml(storyDetail.getContent());
                        mHeaderViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
                        mHeaderViewHolder.mContent.setText(jobContent);
                    }
                    mHeaderViewHolder.mStoryPoints.setText(String.valueOf(storyDetail.getPoints()));
                    mHeaderViewHolder.mStoryLongAgo.setText(" | " + storyDetail.getTimeAgo());
                    mHeaderViewHolder.mCommentsCount.setText(storyDetail.getCommentsCount() + " comments");
                }
                else {
                    mHeaderViewHolder.mContent.setVisibility(View.VISIBLE);
                    Spanned jobContent = Html.fromHtml(storyDetail.getContent());
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
                            mActionBar.setTitle(storyDetail.getTitle());
                        }
                    }
                });
                mOpenLinkDialogButton.setVisibility(View.VISIBLE);
                mOpenLinkDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ("ask".equals(storyDetail.getType())) {
                            mListener.onStoryFragmentInteraction("https://news.ycombinator.com/" + storyDetail.getUrl());
                        }
                        else if ("job".equals(storyDetail.getType())) {
                            if (storyDetail.getUrl().contains("/item?id=")) {
                                mListener.onStoryFragmentInteraction("https://news.ycombinator.com/" + storyDetail.getUrl());
                            }
                            else {
                                mListener.onStoryFragmentInteraction(storyDetail.getUrl());
                            }
                        }
                        else {
                            mListener.onStoryFragmentInteraction(storyDetail.getUrl());
                        }
                    }
                });
                mListAdapter.setStoryDetail(storyDetail);
                mListAdapter.setComments(storyDetail.getCommentList());
                mListAdapter.notifyDataSetChanged();
                showProgress(false);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
                Toast.makeText(getActivity(), "Problem loading page", Toast.LENGTH_SHORT).show();
            }
        });
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
        mListener.onStoryCommentsFragmentDetach();
        mListener = null;
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
                                sendIntent.putExtra(Intent.EXTRA_TEXT, mStoryDetail.getUrl());
                                break;
                            case 1:
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://news.ycombinator.com/item?id=" + mStoryId);
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
                browserIntent.setData(Uri.parse("https://news.ycombinator.com/item?id=" + mStoryId));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        public void onStoryFragmentInteraction(String url);

        public void onStoryCommentsFragmentDetach();
    }

    static class HeaderViewHolder {
        TextView mStoryTitle;
        TextView mStoryDomain;
        TextView mStorySubmitter;
        TextView mStoryPoints;
        TextView mStoryLongAgo;
        TextView mCommentsCount;
        TextView mContent;
    }
}
