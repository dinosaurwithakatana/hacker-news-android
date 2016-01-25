package io.dwak.holohackernews.app.dagger.component;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.PresenterModule;
import io.dwak.holohackernews.app.dagger.scope.TestScope;
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenterImplTest;
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImplTest;

@TestScope
@Component(modules = PresenterModule.class, dependencies = InteractorComponent.class)
public interface TestPresenterComponent extends PresenterComponent {
    void inject(StoryListPresenterImplTest presenterTest);
    void inject(StoryDetailPresenterImplTest presenterImplTest);
}
