package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

class HeaderViewHolder extends RecyclerView.ViewHolder{
    @InjectView(R.id.story_title) TextView mStoryTitle;
    @InjectView(R.id.story_domain) TextView mStoryDomain;
    @InjectView(R.id.story_submitter) TextView mStorySubmitter;
    @InjectView(R.id.story_points) TextView mStoryPoints;
    @InjectView(R.id.story_long_ago) TextView mStoryLongAgo;
    @InjectView(R.id.comment_count) TextView mCommentsCount;
    @InjectView(R.id.job_content) TextView mContent;

    private HeaderViewHolder(View headerView) {
        super(headerView);
        ButterKnife.inject(this, headerView);
    }

    static HeaderViewHolder create(LayoutInflater inflater, ViewGroup parent){
        View headerView = inflater.inflate(R.layout.comments_header, parent, false);
        return new HeaderViewHolder(headerView);
    }

    static void bind(@NonNull Context context, @NonNull HeaderViewHolder headerViewHolder, @NonNull StoryDetail storyDetail){
        if(TextUtils.isEmpty(storyDetail.getTitle())){
            headerViewHolder.mStoryTitle.setVisibility(View.GONE);
        }
        else {
            headerViewHolder.mStoryTitle.setVisibility(View.VISIBLE);
            headerViewHolder.mStoryTitle.setText(storyDetail.getTitle());
        }
        headerViewHolder.mStorySubmitter.setText(storyDetail.getUser());
        if (!StoryDetail.JOB.equals(storyDetail.getType())) {
            headerViewHolder.mContent.setVisibility(View.GONE);
            if (StoryDetail.LINK.equals(storyDetail.getType()) && !TextUtils.isEmpty(storyDetail.getDomain())) {
                String domain = storyDetail.getDomain();
                headerViewHolder.mStoryDomain.setVisibility(View.VISIBLE);
                headerViewHolder.mStoryDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20));
            }
            else {
                headerViewHolder.mStoryDomain.setVisibility(View.GONE);

                if(!TextUtils.isEmpty(storyDetail.getContent())) {
                    headerViewHolder.mContent.setVisibility(View.VISIBLE);
                    Spanned jobContent = Html.fromHtml(storyDetail.getContent());
                    headerViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
                    headerViewHolder.mContent.setText(jobContent);
                    headerViewHolder.mContent.setTextColor(context.getResources().getColor(UserPreferenceManager.getInstance().isNightModeEnabled()
                                                                                           ? android.R.color.white
                                                                                           : android.R.color.black));
                }
            }

            if(storyDetail.getPoints() != null) {
                headerViewHolder.mStoryPoints.setVisibility(View.VISIBLE);
                headerViewHolder.mStoryPoints.setText(String.valueOf(storyDetail.getPoints()));
            }
            else {
                headerViewHolder.mStoryPoints.setVisibility(View.GONE);
            }

            headerViewHolder.mStoryLongAgo.setText(" | " + storyDetail.getTimeAgo());
            headerViewHolder.mCommentsCount.setText(storyDetail.getCommentsCount() + " comments");
        }
        else {
            headerViewHolder.mContent.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(storyDetail.getContent())) {
                headerViewHolder.mContent.setText(Html.fromHtml(storyDetail.getContent()));
            }
            headerViewHolder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
            headerViewHolder.mContent.setTextColor(context.getResources().getColor(android.R.color.black));
            headerViewHolder.mStoryDomain.setVisibility(View.GONE);
            headerViewHolder.mCommentsCount.setVisibility(View.GONE);
            headerViewHolder.mStoryPoints.setVisibility(View.GONE);
        }
    }
}
