package io.dwak.holohackernews.app.ui.about;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AboutItem<T> {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HEADER, ITEM})
    public @interface ViewType{}
    public static final int HEADER = 0;
    public static final int ITEM = 1;

    public final T object;
    public final @AboutItem.ViewType int viewType;

    public AboutItem(T object, int viewType) {
        this.object = object;
        this.viewType = viewType;
    }
}
