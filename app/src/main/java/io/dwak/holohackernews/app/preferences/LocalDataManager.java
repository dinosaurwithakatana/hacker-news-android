package io.dwak.holohackernews.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vishnu on 10/24/14.
 */
public class LocalDataManager {
    public static final String LOCAL_PREFS_NAME = "local_prefs";
    public static final String PREF_RETURNING_USER = "PREF_RETURNING_USER";
    private static LocalDataManager sInstance;
    private final SharedPreferences mPreferences;
    private Context mContext;

    private LocalDataManager(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(LOCAL_PREFS_NAME, 0);
        mPreferences.edit().commit();
    }

    public static void initialize(Context context) {
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
        return get(PREF_RETURNING_USER);
    }

    public void setReturningUser(boolean isFirstRun) {
        set(PREF_RETURNING_USER, isFirstRun);
    }

    private boolean get(String key) {
        return mPreferences.getBoolean(key, false);
    }

    private void set(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
