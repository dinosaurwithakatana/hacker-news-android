package io.dwak.holohackernews.app.ui.storydetail;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by vishnu on 2/2/15.
 */
public class StoryDetailRecyclerItem<T> {
    private T mObject;
    private int mViewType;

    public StoryDetailRecyclerItem(T object, @ViewType int viewType) {
        mObject = object;
        mViewType = viewType;
    }

    public T getObject() {
        return mObject;
    }

    public int getViewType() {
        return mViewType;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_TYPE_HEADER, VIEW_TYPE_COMMENT})
    public @interface ViewType{}
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_COMMENT = 1;
}
