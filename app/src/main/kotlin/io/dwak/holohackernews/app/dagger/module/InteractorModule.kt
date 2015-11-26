package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractorImpl

@Module
open class InteractorModule() {

    @Provides
    fun providesMainThreadScheduler(): RxSchedulerInteractor = getSchedulerInteractor()
    open fun getSchedulerInteractor(): RxSchedulerInteractor = RxSchedulerInteractorImpl

}