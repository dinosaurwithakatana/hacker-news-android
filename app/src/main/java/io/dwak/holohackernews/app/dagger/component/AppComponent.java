package io.dwak.holohackernews.app.dagger.component;

import android.app.Application;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.AppModule;
import io.dwak.holohackernews.app.ui.storylist.MainViewModel;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application application);
    void inject(MainViewModel viewModel);
}
