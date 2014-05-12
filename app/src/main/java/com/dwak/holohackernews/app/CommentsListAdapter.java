package com.dwak.holohackernews.app;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dwak.holohackernews.app.network.models.Comment;
import com.dwak.holohackernews.app.network.models.StoryDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishnu on 5/4/14.
 */
public class CommentsListAdapter extends ArrayAdapter<Comment> {
    private List<Comment> mComments;
    private Context mContext;
    private final int mResource;
    private List<Comment> mExpandedComments;
    private StoryDetail mStoryDetail;

    public CommentsListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mComments = objects;

        mExpandedComments = new ArrayList<Comment>();
    }

    private void expandComments(Comment comment) {
        mExpandedComments.add(comment);

        if (comment.getChildComments().size() == 0) {
            return;
        }

        for (Comment childComment : comment.getChildComments()) {
            expandComments(childComment);
        }

    }

    public void setStoryDetail(StoryDetail storyDetail) {
        mStoryDetail = storyDetail;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        mExpandedComments.clear();
        for (Comment comment : mComments) {
            expandComments(comment);
        }
    }

    @Override
    public int getCount() {
        return mExpandedComments.size();
    }

    @Override
    public Comment getItem(int position) {
        return mExpandedComments.get(position);
    }

    @Override
    public int getPosition(Comment item) {
        return mExpandedComments.indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        return mExpandedComments.get(position).getLevel();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(mResource, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mCommentContent = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder.mColorCodeView = convertView.findViewById(R.id.color_code);
            viewHolder.mCommentSubmissionTime = (TextView) convertView.findViewById(R.id.comment_submission_time);
            viewHolder.mCommentSubmitter = (TextView) convertView.findViewById(R.id.comment_submitter);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Spanned commentContent = Html.fromHtml(getItem(position).getContent());
        viewHolder.mCommentContent.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.mCommentContent.setText(commentContent);
        viewHolder.mCommentSubmissionTime.setText(getItem(position).getTimeAgo());

        String submitter = getItem(position).getUser();
        if (HoloHackerNewsApplication.isDebug()) {
            viewHolder.mCommentSubmitter.setText(position + " " + submitter);
        }
        else {
            viewHolder.mCommentSubmitter.setText(submitter);
        }
        viewHolder.mCommentSubmitter.setTextColor(
                mContext.getResources().getColor(
                        mStoryDetail.getUser().equals(submitter)
                                ? android.R.color.holo_orange_light
                                : android.R.color.black
                )
        );

        float scale = mContext.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (getItem(position).getLevel() * 12 * scale + 0.5f);

        if (getItem(position).getLevel() != 0) {
            convertView.setPadding(dpAsPixels, 0, 4, 0);
        }
        else {
            convertView.setPadding(4, 0, 4, 0);
        }

        switch (getItem(position).getLevel() % 8) {
            case 0:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_blue_bright);
                break;
            case 1:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_green_light);
                break;
            case 2:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_red_light);
                break;
            case 3:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_orange_light);
                break;
            case 4:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_purple);
                break;
            case 5:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_green_dark);
                break;
            case 6:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_red_dark);
                break;
            case 7:
                viewHolder.mColorCodeView.setBackgroundResource(android.R.color.holo_orange_dark);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView mCommentContent;
        View mColorCodeView;
        TextView mCommentSubmitter;
        TextView mCommentSubmissionTime;
    }
}
