package io.dwak.holohackernews.app.dagger.component;

import android.app.Application;

import javax.inject.Named;

import dagger.Component;
import io.dwak.holohackernews.app.cache.CacheManager;
import io.dwak.holohackernews.app.dagger.module.AppModule;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application application);
    @Named("cacheManager")
    CacheManager getCacheManager();
}
