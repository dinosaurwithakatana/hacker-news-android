package io.dwak.holohackernews.app.dagger;

import android.app.Application;

import dagger.Component;

@Component(modules = {HackerNewsServiceModule.class})
public interface AppComponent {
    void inject(Application application);
}
