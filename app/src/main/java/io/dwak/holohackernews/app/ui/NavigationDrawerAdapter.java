package io.dwak.holohackernews.app.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.dwak.holohackernews.app.R;

/**
 * Created by vishnu on 9/2/14.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {
    private final Context mContext;
    private final int mResource;
    private final List<NavigationDrawerItem> mNavigationDrawerItems;

    public NavigationDrawerAdapter(Context context, int resource, List<NavigationDrawerItem> navigationDrawerItems) {
        super(context, resource, navigationDrawerItems);
        mContext = context;
        mResource = resource;
        mNavigationDrawerItems = navigationDrawerItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (getItem(position).shouldDisplayIcon()) {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.secondary_navigation_item, null);
            TextView secondaryNavigationTitle = (TextView) view.findViewById(R.id.secondary_navigation_title);
            secondaryNavigationTitle.setText(getItem(position).getTitle());
            secondaryNavigationTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(getItem(position).getIconResId()), null, null, null);
        }
        else {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.navigation_item, null);
            ((TextView) view.findViewById(R.id.navigation_title)).setText(getItem(position).getTitle());
        }
        return view;
    }
}
