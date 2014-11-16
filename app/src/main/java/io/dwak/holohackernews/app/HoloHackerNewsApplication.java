package io.dwak.holohackernews.app;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by vishnu on 5/3/14.
 */
public class HoloHackerNewsApplication extends Application {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static HoloHackerNewsApplication sInstance;
    private Context mContext;
    private HackerNewsService mHackerNewsService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }

        mContext = getApplicationContext();
        LocalDataManager.initialize(mContext);
    }

    public static boolean isDebug() {
        return mDebug;
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
}
