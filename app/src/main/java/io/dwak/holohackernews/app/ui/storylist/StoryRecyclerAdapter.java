package io.dwak.holohackernews.app.ui.storylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dwak.holohackernews.app.models.Story;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryListViewHolder> {
    @NonNull private final Context mContext;
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
        viewHolder.mView.setOnClickListener(v -> mListener.onStoryClick(position));
        viewHolder.mTitle.setText(getItem(position).getTitle());
        viewHolder.mSubmittedBy.setText(getItem(position).getSubmitter());
        viewHolder.mSubmissionTime.setText(getItem(position).getPublishedTime());
        if (getItem(position).getType().equals("link")) {
            String domain = getItem(position).getDomain();
            if(!TextUtils.isEmpty(domain)){
                viewHolder.mDomain.setVisibility(View.VISIBLE);
                viewHolder.mDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20) + " | ");
            }
            else{
                viewHolder.mDomain.setVisibility(View.GONE);
            }
        }
        else{
            viewHolder.mDomain.setText(" | " + getItem(position).getType() + " | ");
        }
        viewHolder.mPoints.setText(String.valueOf(getItem(position).getPoints()));
        viewHolder.mCommentsCount.setText(getItem(position).getNumComments() + " comments");
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    @Override
    public long getItemId(int position) {
        return mStoryList.get(position).getStoryId();
    }

    public Story getItem(int position){
        return mStoryList.get(position);
    }

    public void clear(){
        mStoryList.clear();
        notifyDataSetChanged();
    }

    public void addStories(@NonNull List<Story> storyList){
        mStoryList.addAll(storyList);
        notifyDataSetChanged();
    }

    public int getPositionOfItem(@NonNull Story story){
        return mStoryList.indexOf(story);
    }

    public void addStory(@NonNull Story story){
        mStoryList.add(story);
        notifyItemInserted(mStoryList.size());
    }

    public interface StoryListAdapterListener{
        void onStoryClick(int position);
    }
}
