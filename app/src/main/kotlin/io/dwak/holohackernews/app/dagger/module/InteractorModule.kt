package io.dwak.holohackernews.app.dagger.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractorImpl

@Module
open class InteractorModule(private val context : Context) {

    @Provides open fun providesMainThreadScheduler(): RxSchedulerInteractor = RxSchedulerInteractorImpl
    @Provides open fun resources() : Resources = context.resources

}