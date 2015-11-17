package io.dwak.holohackernews.app.dagger.component;

import android.app.Application;
import android.content.res.Resources;

import javax.inject.Named;

import dagger.Component;
import io.dwak.holohackernews.app.cache.CacheManager;
import io.dwak.holohackernews.app.dagger.module.AppModule;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application application);

    @Named(AppModule.CACHE_MANAGER)
    CacheManager getCacheManager();

    @Named(AppModule.RESOURCES)
    Resources getResources();
}
