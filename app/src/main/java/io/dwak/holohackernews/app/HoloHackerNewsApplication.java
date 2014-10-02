package io.dwak.holohackernews.app;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.ReadabilityService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by vishnu on 5/3/14.
 */
public class HoloHackerNewsApplication extends Application {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static String READABILITY_TOKEN = BuildConfig.READABILITY_TOKEN;
    private static boolean TRAVIS = BuildConfig.TRAVIS;
    private static HoloHackerNewsApplication sInstance;
    private Context mContext;
    private HackerNewsService mHackerNewsService;
    private ReadabilityService mReadabilityService;
    private boolean mReadabilityEnabled = false;

    public static String getREADABILITY_TOKEN() {
        return READABILITY_TOKEN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }

        if (!HoloHackerNewsApplication.isTRAVIS()) {
            if(mReadabilityService == null) {
                RestAdapter readabilityRestAdapter = new RestAdapter.Builder()
                        .setEndpoint("https://readability.com/api/content/v1/")
                        .build();

                mReadabilityService = readabilityRestAdapter.create(ReadabilityService.class);

                mReadabilityEnabled = true;
            }
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

    public HackerNewsService getHackerNewsServiceInstance(){
        if(mHackerNewsService==null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Long.class, new LongTypeAdapter());
            Gson gson = gsonBuilder.create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setEndpoint("http://fathomless-island-9288.herokuapp.com/")
                    .build();

            mHackerNewsService = restAdapter.create(HackerNewsService.class);
        }

        return mHackerNewsService;
    }

    public ReadabilityService getReadabilityService(){
        return mReadabilityService;
    }

    public boolean isReadabilityEnabled() {
        return mReadabilityEnabled;
    }
}
