package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.Story;

/**
 * Created by vishnu on 5/3/14.
 */
public class StoryListAdapter extends ArrayAdapter<Story> {

    private Context mContext;
    private int mResource;

    public StoryListAdapter(Context context, int resource, List<Story> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public long getItemId(int location) {
        return getItem(location).getStoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

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

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.story_title) TextView mTitle;
        @InjectView(R.id.story_submitter) TextView mSubmittedBy;
        @InjectView(R.id.story_long_ago) TextView mSubmissionTime;
        @InjectView(R.id.story_domain) TextView mDomain;
        @InjectView(R.id.story_points) TextView mPoints;
        @InjectView(R.id.comment_count) TextView mCommentsCount;
        View mView;

        public ViewHolder(View view) {
            mView = view;
            ButterKnife.inject(this, view);
        }
    }

}
