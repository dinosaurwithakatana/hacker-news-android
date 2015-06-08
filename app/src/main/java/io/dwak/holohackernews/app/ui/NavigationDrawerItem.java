package io.dwak.holohackernews.app.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class NavigationDrawerItem implements IDrawerItem{
    
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

    @Override
    public int getIdentifier() {
        return mId;
    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public View convertView(LayoutInflater layoutInflater, View view, ViewGroup viewGroup) {
        return null;
    }
}
