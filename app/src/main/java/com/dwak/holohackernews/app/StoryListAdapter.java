package com.dwak.holohackernews.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dwak.holohackernews.app.network.models.Story;

import java.util.List;

/**
* Created by vishnu on 5/3/14.
*/
public class StoryListAdapter extends ArrayAdapter<Story> {

    private Context mContext;
    private int mResource;
    private List<Story> mStories;

    public StoryListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public StoryListAdapter(Context context, int resource, List<Story> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mStories = objects;
    }

    public void setStories(List<Story> stories) {
        mStories = stories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public long getItemId(int position) {
        return mStories.get(position).getStoryId();
    }

    @Override
    public Story getItem(int position) {
        return mStories.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mResource, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.story_title);
            viewHolder.mSubmittedBy = (TextView) convertView.findViewById(R.id.story_submitter);
            viewHolder.mSubmissionTime = (TextView) convertView.findViewById(R.id.story_long_ago);
            viewHolder.mDomain = (TextView) convertView.findViewById(R.id.story_domain);
            viewHolder.mPoints = (TextView) convertView.findViewById(R.id.story_points);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTitle.setText(getItem(position).getTitle());
        viewHolder.mSubmittedBy.setText(getItem(position).getSubmitter());
        viewHolder.mSubmissionTime.setText(getItem(position).getPublishedTime());
        viewHolder.mDomain.setText(" | " + getItem(position).getDomain()+ " | ");
        viewHolder.mPoints.setText(String.valueOf(getItem(position).getPoints()));

        return convertView;
    }

    static class ViewHolder {
        TextView mTitle;
        TextView mSubmittedBy;
        TextView mSubmissionTime;
        TextView mDomain;
        TextView mPoints;
    }

}
