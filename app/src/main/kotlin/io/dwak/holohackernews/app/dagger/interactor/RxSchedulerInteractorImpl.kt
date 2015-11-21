package io.dwak.holohackernews.app.dagger.interactor

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers

class RxSchedulerInteractorImpl {
    companion object : RxSchedulerInteractor {
        override val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()
    }
}