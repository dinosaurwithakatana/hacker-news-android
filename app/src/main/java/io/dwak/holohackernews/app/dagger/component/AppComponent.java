package io.dwak.holohackernews.app.dagger.component;

import android.app.Application;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.AppModule;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application application);
}
