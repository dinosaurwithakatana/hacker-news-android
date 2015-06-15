package io.dwak.holohackernews.app.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.ui.login.LoginActivity;

/**
 * Created by vishnu on 10/24/14.
 */
public class LocalDataManager {
    public static final String LOCAL_PREFS_NAME = "local_prefs";
    public static final String PREF_RETURNING_USER = "PREF_RETURNING_USER";
    public static final String PREF_USER_LOGIN_COOKIE = "PREF_USER_LOGIN_COOKIE";
    private static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String CURRENT_USER = "CURRENT_USER";
    private static LocalDataManager sInstance;
    private final SharedPreferences mPreferences;
    private Context mContext;
    private String mUserName;

    private LocalDataManager(@NonNull Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(LOCAL_PREFS_NAME, 0);
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

    private void remove(@NonNull String key){
        mPreferences.edit().remove(key).apply();
    }

    public void setUserLoginCookie(String userLoginCookie) {
        set(PREF_USER_LOGIN_COOKIE, userLoginCookie);
    }

    @Nullable
    public String getUserLoginCookie(){
        return getString(PREF_USER_LOGIN_COOKIE);
    }

    public void setUserName(String userName) {
        set(PREF_USERNAME, userName);
    }

    public String getUserName(){
        return getString(PREF_USERNAME);
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        set(CURRENT_USER, json);
    }

    public User getUser(){
        return new Gson().fromJson(getString(CURRENT_USER), User.class);
    }

    public void logoutUser(){
        remove(CURRENT_USER);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(LoginActivity.LOGOUT));
    }
}
