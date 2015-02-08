package io.dwak.holohackernews.app.ui.storydetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.util.UIUtils;

/**
* Created by vishnu on 2/2/15.
*/
class CommentViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.comment_content) TextView mCommentContent;
    @InjectView(R.id.comment_submission_time) TextView mCommentSubmissionTime;
    @InjectView(R.id.comment_submitter) TextView mCommentSubmitter;
    @InjectView(R.id.comment_overflow) ImageButton mOverflow;
    @InjectView(R.id.comments_container) View mCommentsContainer;
    @InjectView(R.id.color_code) View mColorCode;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    static CommentViewHolder create(LayoutInflater inflater) {
        View commentItemView = inflater.inflate(R.layout.comments_list_item, null);
        CommentViewHolder commentViewHolder = new CommentViewHolder(commentItemView);
        return commentViewHolder;
    }

    static void bind(Context context, CommentViewHolder viewHolder, Comment comment) {
        final Spanned commentContent = Html.fromHtml(comment.getContent());
        viewHolder.mCommentContent.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.mCommentContent.setText(commentContent);
        viewHolder.mCommentSubmissionTime.setText(comment.getTimeAgo());
        viewHolder.mOverflow.setOnClickListener(view -> commentAction(context, comment));

        String submitter = comment.getUser();
        if (HoloHackerNewsApplication.isDebug()) {
            viewHolder.mCommentSubmitter.setText(viewHolder.getPosition() + " " + submitter);
        }
        else {
        viewHolder.mCommentSubmitter.setText(submitter);
        }
        viewHolder.mCommentSubmitter.setTextColor(
                context.getResources().getColor(
                        comment.isOriginalPoster()
                                ? android.R.color.holo_orange_light
                                : android.R.color.black
                )
        );
        int colorCodeLeftMargin = UIUtils.dpAsPixels(context, comment.getLevel() * 12);
        int contentLeftMargin = UIUtils.dpAsPixels(context, 4);

        if (comment.getLevel() != 0) {
            FrameLayout.LayoutParams commentsContainerLayoutParams= new FrameLayout.LayoutParams(viewHolder.mCommentsContainer.getLayoutParams());
            commentsContainerLayoutParams.setMargins(contentLeftMargin,
                    commentsContainerLayoutParams.topMargin,
                    commentsContainerLayoutParams.rightMargin,
                    commentsContainerLayoutParams.bottomMargin);
            viewHolder.mCommentsContainer.setLayoutParams(commentsContainerLayoutParams);

            FrameLayout.LayoutParams colorCodeLayoutParams = new FrameLayout.LayoutParams(viewHolder.mColorCode.getLayoutParams());
            colorCodeLayoutParams.setMargins(colorCodeLeftMargin,
                    colorCodeLayoutParams.topMargin,
                    colorCodeLayoutParams.rightMargin,
                    colorCodeLayoutParams.bottomMargin);
            viewHolder.mColorCode.setLayoutParams(colorCodeLayoutParams);

        }
        else {
            FrameLayout.LayoutParams commentsContainerLayoutParams= new FrameLayout.LayoutParams(viewHolder.mCommentsContainer.getLayoutParams());
            commentsContainerLayoutParams.setMargins(0,
                    commentsContainerLayoutParams.topMargin,
                    commentsContainerLayoutParams.rightMargin,
                    commentsContainerLayoutParams.bottomMargin);
            viewHolder.mCommentsContainer.setLayoutParams(commentsContainerLayoutParams);

            FrameLayout.LayoutParams colorCodeLayoutParams = new FrameLayout.LayoutParams(viewHolder.mColorCode.getLayoutParams());
            colorCodeLayoutParams.setMargins(0,
                    colorCodeLayoutParams.topMargin,
                    colorCodeLayoutParams.rightMargin,
                    colorCodeLayoutParams.bottomMargin);
            viewHolder.mColorCode.setLayoutParams(colorCodeLayoutParams);
        }
        switch (comment.getLevel() % 8) {
            case 0:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_blue_bright);
                break;
            case 1:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_green_light);
                break;
            case 2:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_red_light);
                break;
            case 3:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_orange_light);
                break;
            case 4:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_purple);
                break;
            case 5:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_green_dark);
                break;
            case 6:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_red_dark);
                break;
            case 7:
                viewHolder.mColorCode.setBackgroundResource(android.R.color.holo_orange_dark);
                break;
        }
    }

    static void commentAction(Context context, Comment comment) {
        final CharSequence[] commentActions = {"Share Comment", "Share Comment Content"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(commentActions, (dialogInterface, j) -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            switch (j) {
                case 0:
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "https://news.ycombinator.com/item?id=" + comment.getId());
                    break;
                case 1:
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            comment.getUser() + ": " + Html.fromHtml(comment.getContent()));
                    break;
            }
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        });

        builder.create().show();
    }

}
