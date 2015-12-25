package io.dwak.holohackernews.app.dagger.module;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor;
import io.dwak.holohackernews.app.dagger.interactor.TestRxSchedulerImpl;

public class TestInteractorModule extends InteractorModule {
    public TestInteractorModule(@NotNull Context context) {
        super(context);
    }

    @NotNull
    @Override
    public RxSchedulerInteractor providesMainThreadScheduler() {
        return new TestRxSchedulerImpl();
    }
}
