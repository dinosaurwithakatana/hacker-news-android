package io.dwak.holohackernews.app.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by vishnu on 9/2/14.
 */
public class NavigationDrawerItem {
    private int mId;
    @DrawableRes private int mIconResId;
    @NonNull private String mTitle;
    private boolean mShouldDisplayIcon;

    public NavigationDrawerItem(int id, @DrawableRes int iconResId, @NonNull String title, boolean displayIcon) {
        mId = id;
        mIconResId = iconResId;
        mTitle = title;
        mShouldDisplayIcon = displayIcon;
    }

    public @DrawableRes int getIconResId() {
        return mIconResId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public boolean shouldDisplayIcon() {
        return mShouldDisplayIcon;
    }

    public int getId() {
        return mId;
    }
}
