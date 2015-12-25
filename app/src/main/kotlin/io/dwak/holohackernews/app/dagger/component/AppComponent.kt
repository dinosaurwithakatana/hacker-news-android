package io.dwak.holohackernews.app.dagger.component

import android.content.Context
import dagger.Component
import io.dwak.holohackernews.app.HackerNewsApplication
import io.dwak.holohackernews.app.dagger.module.AppModule
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(storyItemPresenter : StoryItemPresenterImpl)

    val app : HackerNewsApplication
}