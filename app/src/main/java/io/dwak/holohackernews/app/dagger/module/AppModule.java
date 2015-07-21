package io.dwak.holohackernews.app.dagger.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.cache.CacheManager;
import io.dwak.holohackernews.app.network.LoganSquareConvertor;
import io.dwak.holohackernews.app.network.LongTypeAdapter;
import retrofit.RestAdapter;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Named("context")
    Context providesApplication() {
        return mApplication;
    }


    @Provides
    @Named("resources")
    Resources providesResources() {
        return mApplication.getResources();
    }

    @Provides
    @Named("gson")
    Gson providesGson() {
        return new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter())
                                .create();
    }

    @Provides
    @Singleton
    @Named("loganSquare")
    LoganSquareConvertor providesLoganSquare(){
        return new LoganSquareConvertor();
    }

    @Provides
    @Named("retrofit-loglevel")
    RestAdapter.LogLevel providesLogLevel(){
        return RestAdapter.LogLevel.FULL;
    }

    @Provides
    @Named("cacheManager")
    CacheManager providesCacheManager(@Named("context")Context context,
                                      @Named("gson") Gson gson){
        return CacheManager.getInstance(context, gson);
    }
}
