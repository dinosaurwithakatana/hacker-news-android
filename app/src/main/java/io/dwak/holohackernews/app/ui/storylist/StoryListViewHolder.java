package io.dwak.holohackernews.app.ui.storylist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;

public class StoryListViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.story_title) TextView title;
    @InjectView(R.id.story_submitter) TextView submittedBy;
    @InjectView(R.id.story_long_ago) TextView submissionTime;
    @InjectView(R.id.story_domain) TextView domain;
    @InjectView(R.id.story_points) TextView points;
    @InjectView(R.id.comment_count) TextView commentsCount;
    @InjectView(R.id.bottom_container) View bottomContainer;
    @InjectView(R.id.top_container) View topContainer;
    @InjectView(R.id.save_story_button) TextView saveStory;
    SwipeLayout swipeLayout;
    public boolean isOpening;
    public boolean isReleased;
    public boolean hasBeenDragged;

    public StoryListViewHolder(View itemView) {
        super(itemView);
        swipeLayout = (SwipeLayout) itemView;
        ButterKnife.inject(this, itemView);
    }
}
