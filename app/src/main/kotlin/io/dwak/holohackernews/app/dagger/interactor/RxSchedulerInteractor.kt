package io.dwak.holohackernews.app.dagger.interactor

import rx.Scheduler

public interface RxSchedulerInteractor {
    val mainThreadScheduler: Scheduler
}