package io.dwak.holohackernews.app.base;

import javax.inject.Inject;

import io.dwak.holohackernews.app.base.mvp.Presenter;
import io.dwak.holohackernews.app.dagger.component.DaggerTestNetworkComponent;
import io.dwak.holohackernews.app.dagger.component.DaggerTestPresenterComponent;
import io.dwak.holohackernews.app.dagger.component.TestPresenterComponent;
import io.dwak.holohackernews.app.dagger.module.PresenterModule;
import io.dwak.holohackernews.app.dagger.module.TestInteractorModule;
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule;
import io.dwak.holohackernews.app.ui.list.view.StoryListView;

public abstract class BasePresenterTest<T extends Presenter> {
    @Inject T presenter;

    public T getPresenter() {
        return presenter;
    }

    protected TestPresenterComponent getComponent(StoryListView view, TestNetworkModule testNetworkModule) {
        return DaggerTestPresenterComponent.builder()
                                    .presenterModule(new PresenterModule(view))
                                    .networkComponent(DaggerTestNetworkComponent.builder()
                                                                                .networkModule(testNetworkModule)
                                                                                .interactorModule(new TestInteractorModule())
                                                                                .build())
                                    .build();
    }
}
