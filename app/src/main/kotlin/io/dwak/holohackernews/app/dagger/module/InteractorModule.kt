package io.dwak.holohackernews.app.dagger.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.dagger.interactor.*
import io.dwak.holohackernews.app.manager.UserPreferenceManager

@Module
open class InteractorModule(private val context : Context) {

  @Provides open fun providesMainThreadScheduler() : RxSchedulerInteractor
          = RxSchedulerInteractorImpl

  @Provides open fun resources() : Resources = context.resources
  @Provides open fun htmlParser() : HtmlInteractor.HtmlParser = HtmlInteractorImpl
  @Provides open fun userPreferences() : UserPreferenceInteractor = UserPreferenceManager

}