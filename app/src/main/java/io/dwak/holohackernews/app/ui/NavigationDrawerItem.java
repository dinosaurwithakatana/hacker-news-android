package io.dwak.holohackernews.app.ui;

import android.support.annotation.NonNull;

/**
 * Created by vishnu on 9/2/14.
 */
public class NavigationDrawerItem {
    @NonNull private int mId;
    @NonNull private int mIconResId;
    @NonNull private String mTitle;
    @NonNull private boolean mShouldDisplayIcon;

    public NavigationDrawerItem(@NonNull int id, @NonNull int iconResId, @NonNull String title, @NonNull boolean displayIcon) {
        mId = id;
        mIconResId = iconResId;
        mTitle = title;
        mShouldDisplayIcon = displayIcon;
    }

    @NonNull
    public int getIconResId() {
        return mIconResId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public boolean shouldDisplayIcon() {
        return mShouldDisplayIcon;
    }

    @NonNull
    public int getId() {
        return mId;
    }
}