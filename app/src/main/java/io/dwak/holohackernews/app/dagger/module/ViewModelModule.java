package io.dwak.holohackernews.app.dagger.module;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.ui.about.AboutViewModel;
import io.dwak.holohackernews.app.ui.login.LoginViewModel;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailViewModel;
import io.dwak.holohackernews.app.ui.storylist.MainViewModel;
import io.dwak.holohackernews.app.ui.storylist.StoryListViewModel;

@Module
public class ViewModelModule {
    @Provides
    StoryListViewModel providesStoryListViewModel() {
        return new StoryListViewModel();
    }

    @Provides
    StoryDetailViewModel providesStoryDetailViewModel() {
        return new StoryDetailViewModel();
    }

    @Provides
    LoginViewModel providesLoginViewModel() {
        return new LoginViewModel();
    }

    @Provides
    MainViewModel providesMainViewModel(@Named("resources") Resources resources) {
        return new MainViewModel(resources);
    }

    @Provides
    AboutViewModel providesAboutViewModel(){
        return new AboutViewModel();
    }
}
