package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.util.UIUtils;

/**
* Created by vishnu on 2/2/15.
*/
class HeaderViewHolder extends RecyclerView.ViewHolder{
    @InjectView(R.id.story_title) TextView mStoryTitle;
    @InjectView(R.id.story_domain) TextView mStoryDomain;
    @InjectView(R.id.story_submitter) TextView mStorySubmitter;
    @InjectView(R.id.story_points) TextView mStoryPoints;
    @InjectView(R.id.story_long_ago) TextView mStoryLongAgo;
    @InjectView(R.id.comment_count) TextView mCommentsCount;
    @InjectView(R.id.job_content) TextView mContent;
    View mHeaderView;

    public HeaderViewHolder(View headerView) {
        super(headerView);
        mHeaderView = headerView;
        ButterKnife.inject(this, headerView);
    }

    static HeaderViewHolder create(Context context, View headerView){
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(headerView);
        headerView.setBackgroundColor(UserPreferenceManager.isNightModeEnabled(context)
                ? context.getResources().getColor(R.color.cardBackgroundNight)
                : context.getResources().getColor(R.color.cardBackground));
        UIUtils.setTextColor(context,
                headerViewHolder.mCommentsCount,
                headerViewHolder.mContent,
                headerViewHolder.mStoryDomain,
                headerViewHolder.mStoryLongAgo,
                headerViewHolder.mStoryPoints,
                headerViewHolder.mStoryTitle);
        return headerViewHolder;
    }

    static void bind(){
    }
}
