package io.dwak.holohackernews.app.ui.storylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Story;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryListViewHolder> {
    @NonNull
    private final Context mContext;
    private final List<Story> mStoryList;
    private int mResource;
    private StoryListAdapterListener mListener;

    public StoryRecyclerAdapter(@NonNull Context context,
                                List<Story> storyList,
                                int resource,
                                StoryListAdapterListener listener) {
        mContext = context;
        mStoryList = storyList;
        mResource = resource;
        mListener = listener;
    }

    @Override
    public StoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new StoryListViewHolder(inflater.inflate(mResource, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryListViewHolder viewHolder, int position) {
        viewHolder.topContainer.setOnClickListener(v -> {
            if (!viewHolder.isOpening && viewHolder.isReleased) {
                if(viewHolder.hasBeenDragged){
                    viewHolder.hasBeenDragged = false;
                    return;
                }
                mListener.onStoryClick(position);
            }
        });
        Story story = getItem(position);
        viewHolder.title.setText(story.getTitle());
        viewHolder.submittedBy.setText(story.getSubmitter());
        viewHolder.submissionTime.setText(story.getPublishedTime());
        if (story.getType().equals("link")) {
            String domain = story.getDomain();
            if (!TextUtils.isEmpty(domain)) {
                viewHolder.domain.setVisibility(View.VISIBLE);
                viewHolder.domain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20) + " | ");
            }
            else {
                viewHolder.domain.setVisibility(View.GONE);
            }
        }
        else {
            viewHolder.domain.setText(" | " + story.getType() + " | ");
        }
        viewHolder.points.setText(String.valueOf(story.getPoints()));
        viewHolder.commentsCount.setText(story.getNumComments() + " comments");
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.bottomContainer);
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                viewHolder.isReleased = false;
                viewHolder.isOpening = true;
                viewHolder.hasBeenDragged = true;
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {
                viewHolder.isReleased = false;
            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                viewHolder.isOpening = false;
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {
                viewHolder.isReleased = true;
            }
        });

        viewHolder.saveStory.setText(story.isSaved() ? "Delete" : "Save");
        viewHolder.saveStory.setCompoundDrawablesRelativeWithIntrinsicBounds(0, story.isSaved() ? R.drawable.ic_action_delete_black : R.drawable.ic_action_archive_black, 0, 0);
        viewHolder.saveStory.setOnClickListener(v -> mListener.onStorySave(position, !story.isSaved()));
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    @Override
    public long getItemId(int position) {
        return mStoryList.get(position).getStoryId();
    }

    public Story getItem(int position) {
        return mStoryList.get(position);
    }

    public void clear() {
        mStoryList.clear();
        notifyDataSetChanged();
    }

    public void addStories(@NonNull List<Story> storyList) {
        mStoryList.addAll(storyList);
        notifyDataSetChanged();
    }

    public int getPositionOfItem(@NonNull Story story) {
        return mStoryList.indexOf(story);
    }

    public void addStory(@NonNull Story story) {
        mStoryList.add(story);
        notifyItemInserted(mStoryList.size());
    }

    public interface StoryListAdapterListener {
        void onStoryClick(int position);
        void onStorySave(int position, boolean save);
    }
}
