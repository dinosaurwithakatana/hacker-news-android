package io.dwak.holohackernews.app.ui.storylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import io.dwak.holohackernews.app.models.Story;

public class StoryListAdapter extends RecyclerView.Adapter<StoryListViewHolder> {
    @NonNull
    private final Context mContext;
    private final List<Story> mStoryList;
    private StoryListAdapterListener mListener;
    private boolean mNightMode;

    public StoryListAdapter(@NonNull Context context,
                            List<Story> storyList,
                            StoryListAdapterListener listener,
                            boolean nightMode) {
        mContext = context;
        mStoryList = storyList;
        mListener = listener;
        mNightMode = nightMode;
    }

    @Override
    public StoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StoryListViewHolder.create(mContext, parent);
    }

    @Override
    public void onBindViewHolder(StoryListViewHolder viewHolder, int position) {
        StoryListViewHolder.bind(viewHolder,
                                 position,
                                 getItem(position),
                                 mListener,
                                 mNightMode);
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

    public void removeItem(int position){
        mStoryList.remove(position);
        notifyItemRemoved(position);
    }

    public interface StoryListAdapterListener {
        void onStoryClick(int position);
        void onStorySave(int position, boolean save);
    }
}
