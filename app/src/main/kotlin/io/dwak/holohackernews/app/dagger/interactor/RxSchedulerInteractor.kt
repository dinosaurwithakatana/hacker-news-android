package io.dwak.holohackernews.app.dagger.interactor

import rx.Scheduler

interface RxSchedulerInteractor {
    val mainThreadScheduler: Scheduler
}