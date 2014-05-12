package io.dwak.holohackernews.app;

import android.app.Application;
import io.dwak.holohackernews.app.BuildConfig;

/**
 * Created by vishnu on 5/3/14.
 */
public class HoloHackerNewsApplication extends Application {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static HoloHackerNewsApplication mInstance = new HoloHackerNewsApplication();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean isDebug() {
        return mDebug;
    }

    public static HoloHackerNewsApplication getInstance() {
        if (mInstance == null) {
            mInstance = new HoloHackerNewsApplication();
        }

        return mInstance;
    }
}
