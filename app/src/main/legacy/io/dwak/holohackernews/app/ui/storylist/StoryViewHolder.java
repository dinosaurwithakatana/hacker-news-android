package io.dwak.holohackernews.app.ui.storylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Story;

public class StoryViewHolder extends RecyclerView.ViewHolder {
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

    public StoryViewHolder(View itemView) {
        super(itemView);
        swipeLayout = (SwipeLayout) itemView;
        ButterKnife.inject(this, itemView);
    }

    public static StoryViewHolder create(@NonNull Context context, @NonNull ViewGroup parent) {
        return new StoryViewHolder(LayoutInflater.from(context).inflate(R.layout.comments_header, parent, false));
    }

    public static void bind(@NonNull StoryViewHolder viewHolder,
                            int position,
                            @NonNull Story story,
                            @NonNull StoryListAdapter.StoryListAdapterListener listener,
                            boolean nightMode) {
        viewHolder.topContainer.setOnClickListener(v -> {
            if (!viewHolder.isOpening && viewHolder.isReleased) {
                if (viewHolder.hasBeenDragged) {
                    viewHolder.hasBeenDragged = false;
                    return;
                }
                listener.onStoryClick(position);
            }
        });
        if(story.isRead()) {
            viewHolder.title
                    .setTextColor(viewHolder.itemView
                                          .getContext()
                                          .getResources()
                                          .getColor(R.color.readTextColor));
        }
        else {
            viewHolder.title
                    .setTextColor(viewHolder.itemView
                                          .getContext()
                                          .getResources()
                                          .getColor(nightMode ? android.R.color.white
                                                              : android.R.color.black));
        }
        viewHolder.title.setText(story.getTitle());
        viewHolder.submittedBy.setText(story.getSubmitter());
        viewHolder.submissionTime.setText(story.getPublishedTime());
        if ("link".equals(story.getType())) {
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

        viewHolder.saveStory.setText(viewHolder.itemView
                                             .getContext()
                                             .getResources()
                                             .getString(story.isSaved() ? R.string.story_action_delete
                                                                        : R.string.story_action_save));
        if (nightMode) {
            viewHolder.saveStory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                                                                                 story.isSaved() ? R.drawable.ic_delete_white
                                                                                                 : R.drawable.ic_archive_white,
                                                                                 0,
                                                                                 0);
        }
        else {
            viewHolder.saveStory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                                                                                 story.isSaved() ? R.drawable.ic_delete_black
                                                                                                 : R.drawable.ic_archive_black,
                                                                                 0,
                                                                                 0);
        }
        viewHolder.saveStory.setOnClickListener(v -> listener.onStorySave(position, !story.isSaved()));

    }
}
