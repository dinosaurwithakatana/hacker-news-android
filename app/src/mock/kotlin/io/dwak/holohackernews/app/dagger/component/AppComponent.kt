package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.HackerNewsApplication
import io.dwak.holohackernews.app.dagger.module.AppModule
import io.dwak.holohackernews.app.dagger.module.MockService
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import timber.log.Timber

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(storyItemPresenter : StoryItemPresenterImpl)
    fun inject(mockService : MockService)

    val app : HackerNewsApplication

    val tree : Timber.Tree

}