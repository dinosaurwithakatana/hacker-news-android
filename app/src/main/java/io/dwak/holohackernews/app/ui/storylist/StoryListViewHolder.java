package io.dwak.holohackernews.app.ui.storylist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.databinding.StoryListItemBinder;
import io.dwak.holohackernews.app.models.Story;

public class StoryListViewHolder extends RecyclerView.ViewHolder {
    public final StoryListItemBinder binder;

    public StoryListViewHolder(StoryListItemBinder binder){
        super(binder.getRoot());
        this.binder = binder;
    }

    public static StoryListViewHolder create(@NonNull Context context, @NonNull ViewGroup parent){
        StoryListItemBinder binder = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_story_list, parent, false);
        binder.executePendingBindings();
        return new StoryListViewHolder(binder);
    }

    public static void bind(StoryListViewHolder viewHolder, Story story, StoryRecyclerAdapter.StoryListAdapterListener listener){
        viewHolder.itemView.setOnClickListener(v -> listener.onStoryClick(viewHolder.getLayoutPosition()));
        viewHolder.binder.setStory(story);
        if (story.getType().equals("link")) {
            String domain = story.getDomain();
            if(!TextUtils.isEmpty(domain)){
                viewHolder.binder.storyDomain.setVisibility(View.VISIBLE);
                viewHolder.binder.storyDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20) + " | ");
            }
            else{
                viewHolder.binder.storyDomain.setVisibility(View.GONE);
            }
        }
        else{
            viewHolder.binder.storyDomain.setText(" | " + story.getType() + " | ");
        }
        viewHolder.binder.commentCount.setText(story.getNumComments() + " comments");
    }
}
