package io.dwak.holohackernews.app.base;

import android.content.Context;

import javax.inject.Inject;

import io.dwak.holohackernews.app.base.mvp.Presenter;
import io.dwak.holohackernews.app.base.mvp.PresenterView;
import io.dwak.holohackernews.app.dagger.component.DaggerTestInteractorComponent;
import io.dwak.holohackernews.app.dagger.component.DaggerTestPresenterComponent;
import io.dwak.holohackernews.app.dagger.component.TestPresenterComponent;
import io.dwak.holohackernews.app.dagger.module.PresenterModule;
import io.dwak.holohackernews.app.dagger.module.TestInteractorModule;
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule;

import static org.mockito.Mockito.mock;

public abstract class BasePresenterTest<T extends Presenter> {
    @Inject T presenter;

    public T getPresenter() {
        return presenter;
    }

    protected TestPresenterComponent getComponent(PresenterView view, TestNetworkModule testNetworkModule) {
        return DaggerTestPresenterComponent.builder()
                                    .presenterModule(new PresenterModule(view))
                                    .interactorComponent(DaggerTestInteractorComponent.builder()
                                                                                .networkModule(testNetworkModule)
                                                                                .interactorModule(new TestInteractorModule(mock(Context.class)))
                                                                                .build())
                                    .build();
    }
}
