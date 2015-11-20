package io.dwak.holohackernews.app.dagger.module

import android.util.Log
import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers

@Module
open class InteractorModule {
    open val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()

    @Provides
    fun providesMainThreadScheduler(): Scheduler {
        return mainThreadScheduler
    }
}