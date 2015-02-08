package io.dwak.holohackernews.app.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

/**
 * Created by vishnu on 9/2/14.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {
    private final Context mContext;

    public NavigationDrawerAdapter(Context context, int resource, List<NavigationDrawerItem> navigationDrawerItems) {
        super(context, resource, navigationDrawerItems);
        mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (getItem(position).shouldDisplayIcon()) {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.secondary_navigation_item, null);
            TextView secondaryNavigationTitle = (TextView) view.findViewById(R.id.secondary_navigation_title);
            secondaryNavigationTitle.setText(getItem(position).getTitle());
            secondaryNavigationTitle.setTextColor(UserPreferenceManager.isNightModeEnabled(mContext) ? mContext.getResources().getColor(android.R.color.white) : mContext.getResources().getColor(android.R.color.black));
            secondaryNavigationTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(getItem(position).getIconResId()), null, null, null);
        }
        else {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.navigation_item, null);
            final TextView textView = (TextView) view.findViewById(R.id.navigation_title);
            textView.setText(getItem(position).getTitle());
            textView.setTextColor(UserPreferenceManager.isNightModeEnabled(mContext) ? mContext.getResources().getColor(android.R.color.white) : mContext.getResources().getColor(android.R.color.black));
        }
        return view;
    }
}
