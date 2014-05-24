package io.dwak.holohackernews.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nhaarman.listviewanimations.ArrayAdapter;
import io.dwak.holohackernews.app.network.models.Story;

import java.util.List;

/**
 * Created by vishnu on 5/3/14.
 */
public class StoryListAdapter extends ArrayAdapter<Story> {

    private Context mContext;
    private int mResource;
    private List<Story> mStories;

    public StoryListAdapter(Context context, int resource, List<Story> objects) {
        mContext = context;
        mResource = resource;
        mStories = objects;
    }

    public void setStories(List<Story> stories) {
        mStories = stories;
        notifyDataSetChanged();
    }

    public void addStories(List<Story> stories) {
        mStories.addAll(stories);
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
            viewHolder.mCommentsCount = (TextView) convertView.findViewById(R.id.comment_count);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }
        else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTitle.setText(getItem(position).getTitle());
        viewHolder.mSubmittedBy.setText(getItem(position).getSubmitter());
        viewHolder.mSubmissionTime.setText(getItem(position).getPublishedTime());
        if (getItem(position).getType().equals("link")) {
            String domain = getItem(position).getDomain();
            viewHolder.mDomain.setText(" | " + domain.substring(0, 20 > domain.length() ? domain.length() : 20) + " | ");
        }
        else{
            viewHolder.mDomain.setText(" | " + getItem(position).getType() + " | ");
        }
        viewHolder.mPoints.setText(String.valueOf(getItem(position).getPoints()));
        viewHolder.mCommentsCount.setText(getItem(position).getNumComments() + " comments");

        return convertView;
    }

    static class ViewHolder {
        TextView mTitle;
        TextView mSubmittedBy;
        TextView mSubmissionTime;
        TextView mDomain;
        TextView mPoints;
        TextView mCommentsCount;
    }

}
