package io.dwak.holohackernews.app.dagger.interactor;

import org.jetbrains.annotations.NotNull;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class TestRxSchedulerImpl implements RxSchedulerInteractor {
    @NotNull
    @Override
    public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
    }
}
