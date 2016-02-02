package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.HackerNewsApplication
import io.dwak.holohackernews.app.dagger.module.AppModule
import io.dwak.holohackernews.app.manager.UserPreferenceManager
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(storyItemPresenter : StoryItemPresenterImpl)
    fun inject(userPreferenceManager : UserPreferenceManager)

    val app : HackerNewsApplication

    val tree : Timber.Tree
}