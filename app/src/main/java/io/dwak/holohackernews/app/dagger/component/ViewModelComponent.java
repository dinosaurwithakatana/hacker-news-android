package io.dwak.holohackernews.app.dagger.component;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.ViewModelModule;
import io.dwak.holohackernews.app.ui.about.AboutActivity;
import io.dwak.holohackernews.app.ui.login.LoginActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailFragment;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;
import io.dwak.holohackernews.app.ui.storylist.StoryListFragment;

@Component(dependencies = AppComponent.class,
           modules = ViewModelModule.class)
public interface ViewModelComponent {
    void inject(MainActivity activity);

    void inject(StoryListFragment fragment);

    void inject(StoryDetailFragment fragment);

    void inject(LoginActivity activity);

    void inject(AboutActivity.AboutFragment aboutFragment);
}
