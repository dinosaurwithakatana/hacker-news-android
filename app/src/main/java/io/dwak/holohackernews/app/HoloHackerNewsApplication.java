package io.dwak.holohackernews.app;

import android.app.Application;
import android.content.Context;

import java.io.File;

/**
 * Created by vishnu on 5/3/14.
 */
public class HoloHackerNewsApplication extends Application {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static String READABILITY_TOKEN = BuildConfig.READABILITY_TOKEN;
    private static boolean TRAVIS = BuildConfig.TRAVIS;
    private static HoloHackerNewsApplication sInstance;
    private Context mContext;

    public static String getREADABILITY_TOKEN() {
        return READABILITY_TOKEN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }

        mContext = getApplicationContext();
    }

    public static boolean isDebug() {
        return mDebug;
    }

    public static boolean isTRAVIS() {
        return TRAVIS;
    }

    public static HoloHackerNewsApplication getInstance() {
        return sInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public File getApplicationCacheDir(){
        return getCacheDir();
    }
}
