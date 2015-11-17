package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.dagger.module.ServiceModule
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ServiceModule::class))
interface ServiceComponent {
    fun inject(storyListPresenterImpl : StoryListPresenterImpl)
    fun inject(storyItemPresenterImpl : StoryItemPresenterImpl)
}