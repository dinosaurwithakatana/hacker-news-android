package io.dwak.holohackernews.app.preferences;

import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.dagger.component.DaggerSharedPreferencesComponent;

public class UserPreferenceManager {

    public static final String SHOULD_USE_EXTERNAL_BROWSER = "pref_system_browser";
    public static final String PREF_LINK_FIRST = "pref_link_first";
    public static final String PREF_LIST_ANIMATIONS = "pref_list_animations";
    public static final String PREF_NIGHT_MODE = "pref_night_mode";
    public static final String PREF_TEXT_SIZE = "pref_text_size";
    public static final String PREF_SWIPE_BACK = "pref_swipe_back";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SMALL, MEDIUM, LARGE})
    public @interface TextSize{}
    public static final String SMALL = "small";
    public static final String MEDIUM = "medium";
    public static final String LARGE = "large";

    public static UserPreferenceManager sInstance;
    @Inject SharedPreferences mSharedPreferences;

    public static UserPreferenceManager getInstance(){
        if(sInstance == null){
            sInstance = new UserPreferenceManager();
        }
        return sInstance;
    }

    public UserPreferenceManager() {
        DaggerSharedPreferencesComponent.builder()
                .appModule(HackerNewsApplication.getAppModule())
                .appComponent(HackerNewsApplication.getAppComponent())
                .build()
                .inject(this);
        sInstance = this;
    }

    public boolean showLinkFirst(){
        return mSharedPreferences.getBoolean(PREF_LINK_FIRST, false);
    }

    public @TextSize String getPreferredTextSize(){
        //noinspection ResourceType
        return mSharedPreferences.getString(PREF_TEXT_SIZE, SMALL);
    }

    public boolean isExternalBrowserEnabled(){
        return mSharedPreferences.getBoolean(SHOULD_USE_EXTERNAL_BROWSER, false);
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public boolean isNightModeEnabled(){
        return mSharedPreferences.getBoolean(PREF_NIGHT_MODE, false);
    }

    public boolean isSwipeBackEnabled(){
        return mSharedPreferences.getBoolean(PREF_SWIPE_BACK, true);
    }
}
