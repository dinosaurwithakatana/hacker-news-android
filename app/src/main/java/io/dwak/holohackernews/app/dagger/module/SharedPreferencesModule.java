package io.dwak.holohackernews.app.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class SharedPreferencesModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(@Named("context") Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
