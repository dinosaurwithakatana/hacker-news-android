package io.dwak.holohackernews.app.base;

import org.junit.Before;

import javax.inject.Inject;

import io.dwak.holohackernews.app.base.base.mvp.Presenter;

public abstract class BasePresenterTest<T extends Presenter> {
    @Inject T presenter;

    @Before
    public void setUp() throws Exception {
        inject();
        presenter.onAttachToView();
    }

    protected abstract void inject();

    public T getPresenter() {
        return presenter;
    }
}
