package io.dwak.holohackernews.app.dagger.module;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor;
import io.dwak.holohackernews.app.dagger.interactor.TestRxSchedulerImpl;

@Module
public class TestInteractorModule extends InteractorModule {
    @NotNull
    @Override
    public RxSchedulerInteractor getSchedulerInteractor() {
        return new TestRxSchedulerImpl();
    }
}
