package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.NetworkServiceModule;
import io.dwak.holohackernews.app.ui.login.LoginViewModel;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailViewModel;
import io.dwak.holohackernews.app.ui.storylist.StoryListViewModel;

@Singleton
@Component(dependencies = AppComponent.class,
           modules = NetworkServiceModule.class)
public interface NetworkServiceComponent {
    void inject(StoryListViewModel viewModel);

    void inject(StoryDetailViewModel viewModel);

    void inject(LoginViewModel viewModel);
}
