package io.dwak.holohackernews.app.dagger;

import dagger.Component;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailViewModel;
import io.dwak.holohackernews.app.ui.storylist.StoryListViewModel;

@Component(modules = {HackerNewsServiceModule.class})
public interface HackerNewsServiceComponent {
    void inject(StoryListViewModel viewModel);
    void inject(StoryDetailViewModel viewModel);
}
