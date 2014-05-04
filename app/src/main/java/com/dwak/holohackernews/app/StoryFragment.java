package com.dwak.holohackernews.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
 * {@link StoryFragment.OnStoryFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STORY_ID = "story_id";

    private long mStoryId;
    private List<Comment> mCommentList;
    private CommentsListAdapter mListAdapter;

    private OnStoryFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mCommentsListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StoryFragment.
     */
    public static StoryFragment newInstance(long param1) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putLong(STORY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public StoryFragment() {
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

        mCommentList = new ArrayList<Comment>();
        mCommentsListView = (ListView) rootView.findViewById(R.id.comments_list);
        mCommentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View actionLayout = view.findViewById(R.id.comment_item_action_layout);
                actionLayout.setVisibility(actionLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                return false;
            }
        });
//        mCommentsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
//                if (i > 2) {
//                    getActivity().getActionBar().hide();
//                }
//                else if (i <= 1) {
//                    getActivity().getActionBar().show();
//                }
//            }
//        });
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
            public void success(StoryDetail storyDetail, Response response) {
                Toast.makeText(getActivity(), storyDetail.getTitle(), Toast.LENGTH_SHORT).show();
                mListAdapter.setComments(storyDetail.getCommentList());
                mListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {

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

    class CommentsListAdapter extends ArrayAdapter<Comment> {
        private List<Comment> mComments;
        private Context mContext;
        private final int mResource;
        private List<Comment> mExpandedComments;

        public CommentsListAdapter(Context context, int resource, List<Comment> objects) {
            super(context, resource, objects);
            mContext = context;
            mResource = resource;
            mComments = objects;

            mExpandedComments = new ArrayList<Comment>();
        }

        private void expandComments(Comment comment) {
            mExpandedComments.add(comment);

            if (comment.getChildComments().size() == 0) {
                return;
            }

            for (Comment childComment : comment.getChildComments()) {
                expandComments(childComment);
            }

        }

        public void setComments(List<Comment> comments) {
            mComments = comments;
            for (Comment comment : mComments) {
                expandComments(comment);
            }
            Log.d("TEST", mExpandedComments.toString());
        }

        @Override
        public int getCount() {
            return mExpandedComments.size();
        }

        @Override
        public Comment getItem(int position) {
            return mExpandedComments.get(position);
        }

        @Override
        public int getPosition(Comment item) {
            return mExpandedComments.indexOf(item);
        }

        @Override
        public int getItemViewType(int position) {
            return mExpandedComments.get(position).getLevel();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = ((Activity) mContext).getLayoutInflater().inflate(mResource, parent, false);

                viewHolder = new ViewHolder();

                viewHolder.mCommentsContent = (TextView) convertView.findViewById(R.id.comment_content);
                viewHolder.mColorCodeView = convertView.findViewById(R.id.color_code);

                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String commentContent = getItem(position).getContent().replace("<p>", "\n");
            viewHolder.mCommentsContent.setText(commentContent);

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (getItem(position).getLevel() * 12 * scale + 0.5f);

            if (getItem(position).getLevel() != 0) {
                convertView.setPadding(dpAsPixels, 0, 4, 0);
            }
            else {
                convertView.setPadding(4, 0, 4, 0);
            }

            switch (getItem(position).getLevel() % 8) {
                case 0:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_blue_bright);
                    break;
                case 1:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_green_light);
                    break;
                case 2:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_red_light);
                    break;
                case 3:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_orange_light);
                    break;
                case 4:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_purple);
                    break;
                case 5:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_green_dark);
                    break;
                case 6:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_red_dark);
                    break;
                case 7:
                    viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_orange_dark);
                    break;
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView mCommentsContent;
        View mColorCodeView;
    }
}
