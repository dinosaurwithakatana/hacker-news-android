package io.dwak.holohackernews.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.dagger.component.DaggerSharedPreferencesComponent;

public class LocalDataManager {
    public static final String PREF_RETURNING_USER = "PREF_RETURNING_USER";
    private static LocalDataManager sInstance;
    @Inject SharedPreferences mPreferences;

    private LocalDataManager(@NonNull Context context) {
        DaggerSharedPreferencesComponent.builder()
                                        .appComponent(HackerNewsApplication.getAppComponent())
                                        .appModule(HackerNewsApplication.getAppModule())
                                        .build()
                                        .inject(this);
        mPreferences.edit().commit();
    }

    public static void initialize(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataManager(context);
        }
        else {
            throw new RuntimeException(LocalDataManager.class.getSimpleName() + " has already been initialized!");
        }
    }

    public static LocalDataManager getInstance() {
        return sInstance;
    }

    public boolean isReturningUser() {
        return getBoolean(PREF_RETURNING_USER);
    }

    public void setReturningUser(boolean isFirstRun) {
        set(PREF_RETURNING_USER, isFirstRun);
    }

    private boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    @Nullable
    private String getString(String key) {
        return mPreferences.getString(key, null);
    }

    private void set(@NonNull String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void set(@NonNull String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void remove(@NonNull String key) {
        mPreferences.edit().remove(key).apply();
    }
}
