package io.dwak.holohackernews.app.ui.storydetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by vishnu on 5/4/14.
 */
public class CommentsListAdapter extends ArrayAdapter<Comment> {
    private final int mResource;
    private final Context mContext;

    public CommentsListAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }

    private void commentAction(final int i) {
        final CharSequence[] commentActions = {"Share Comment", "Share Comment Content"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(commentActions, (dialogInterface, j) -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            switch (j) {
                case 0:
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "https://news.ycombinator.com/item?id=" + getItem(i).getId());
                    break;
                case 1:
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getItem(i).getUser() + ": " + Html.fromHtml(getItem(i).getContent()));
                    break;
            }
            sendIntent.setType("text/plain");
            mContext.startActivity(sendIntent);
        });

        builder.create().show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Spanned commentContent = Html.fromHtml(getItem(position).getContent());
        viewHolder.mCommentContent.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.mCommentContent.setText(commentContent);
        viewHolder.mCommentSubmissionTime.setText(getItem(position).getTimeAgo());
        viewHolder.mOverflow.setOnClickListener(view -> commentAction(position));

        String submitter = getItem(position).getUser();
        if (HoloHackerNewsApplication.isDebug()) {
            viewHolder.mCommentSubmitter.setText(position + " " + submitter);
        }
        else {
            viewHolder.mCommentSubmitter.setText(submitter);
        }
        viewHolder.mCommentSubmitter.setTextColor(
                mContext.getResources().getColor(
                                getItem(position).isOriginalPoster()
                                ? android.R.color.holo_orange_light
                                : android.R.color.black
                )
        );

        int colorCodeLeftMargin = UIUtils.dpAsPixels(mContext, getItem(position).getLevel() * 12);
        int contentLeftMargin = UIUtils.dpAsPixels(mContext, 4);

        if (getItem(position).getLevel() != 0) {
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

        switch (getItem(position).getLevel() % 8) {
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
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.comment_content) TextView mCommentContent;
        @InjectView(R.id.comment_submission_time) TextView mCommentSubmissionTime;
        @InjectView(R.id.comment_submitter)TextView mCommentSubmitter;
        @InjectView(R.id.comment_overflow) ImageButton mOverflow;
        @InjectView(R.id.comments_container) View mCommentsContainer;
        @InjectView(R.id.color_code) View mColorCode;

        public ViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }
}
