package io.dwak.holohackernews.app.ui.storylist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;

public class StoryListViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.story_title) TextView mTitle;
    @InjectView(R.id.story_submitter) TextView mSubmittedBy;
    @InjectView(R.id.story_long_ago) TextView mSubmissionTime;
    @InjectView(R.id.story_domain) TextView mDomain;
    @InjectView(R.id.story_points) TextView mPoints;
    @InjectView(R.id.comment_count) TextView mCommentsCount;
    View mView;

    public StoryListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        mView = itemView;
    }
}
