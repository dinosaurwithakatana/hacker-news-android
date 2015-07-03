package io.dwak.holohackernews.app;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarApp;
import com.squareup.okhttp.OkHttpClient;

import io.dwak.holohackernews.app.dagger.AppComponent;
import io.dwak.holohackernews.app.dagger.DaggerAppComponent;
import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class HackerNewsApplication extends SugarApp {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static HackerNewsApplication sInstance;
    private Context mContext;
    private HackerNewsService mHackerNewsService;
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }

        mContext = getApplicationContext();
        LocalDataManager.initialize(mContext);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                      .enableDumpapp(
                              Stetho.defaultDumperPluginsProvider(this))
                      .enableWebKitInspector(
                              Stetho.defaultInspectorModulesProvider(this))
                      .build());

        sAppComponent = DaggerAppComponent.create();
        sAppComponent.inject(this);
    }

    public static boolean isDebug() {
        return mDebug;
    }

    public static HackerNewsApplication getInstance() {
        return sInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public HackerNewsService getHackerNewsServiceInstance() {
        if (mHackerNewsService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter())
                                         .create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient(new OkHttpClient()))
                    .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                    .build();

            mHackerNewsService = restAdapter.create(HackerNewsService.class);
        }

        return mHackerNewsService;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
