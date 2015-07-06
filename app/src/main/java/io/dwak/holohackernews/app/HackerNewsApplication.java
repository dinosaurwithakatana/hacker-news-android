package io.dwak.holohackernews.app;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.orm.SugarApp;

import io.dwak.holohackernews.app.dagger.component.AppComponent;
import io.dwak.holohackernews.app.dagger.component.DaggerAppComponent;
import io.dwak.holohackernews.app.dagger.module.AppModule;
import io.dwak.holohackernews.app.preferences.LocalDataManager;

public class HackerNewsApplication extends SugarApp {
    private static boolean mDebug = BuildConfig.DEBUG;
    private static HackerNewsApplication sInstance;
    private Context mContext;
    private static AppComponent sAppComponent;

    private static AppModule sAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }

        mContext = getApplicationContext();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                      .enableDumpapp(
                              Stetho.defaultDumperPluginsProvider(this))
                      .enableWebKitInspector(
                              Stetho.defaultInspectorModulesProvider(this))
                      .build());

        sAppModule = new AppModule(this);
        sAppComponent = DaggerAppComponent.builder()
                                          .appModule(sAppModule)
                                          .build();
        sAppComponent.inject(this);

        LocalDataManager.initialize(mContext);
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

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    public static AppModule getAppModule(){
        return sAppModule;
    }
}
