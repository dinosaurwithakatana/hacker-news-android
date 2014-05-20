package io.dwak.holohackernews.app;

import android.app.Application;
import android.os.Build;
import io.dwak.holohackernews.app.BuildConfig;

/**
 * Created by vishnu on 5/3/14.
 */
public class HoloHackerNewsApplication extends Application {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static String READABILITY_TOKEN = BuildConfig.READABILITY_TOKEN;
    private static HoloHackerNewsApplication mInstance = new HoloHackerNewsApplication();

    public static String getREADABILITY_TOKEN() {
        return READABILITY_TOKEN;
    }

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
