package io.dwak.holohackernews.app.base;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public class BaseViewModel {
    private Resources mResources;

    public void setResources(@NonNull Resources resources){
        mResources = resources;
    }

    public Resources getResources() {
        return mResources;
    }

    public void onAttachToView() {

    }

    public void onDetachFromView() {
        mResources = null;
    }
}
