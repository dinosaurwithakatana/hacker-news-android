package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractorImpl

@Module
open class InteractorModule() {
    open fun getSchedulerInteractor(): RxSchedulerInteractor {
        return RxSchedulerInteractorImpl
    }

    @Provides
    fun providesMainThreadScheduler(): RxSchedulerInteractor {
        return getSchedulerInteractor()
    }
}