package io.dwak.holohackernews.app.dagger.module

import android.view.ViewStructure
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.base.base.mvp.PresenterView
import io.dwak.holohackernews.app.dagger.component.ServiceComponent
import io.dwak.holohackernews.app.dagger.scope.ViewScope
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import io.dwak.holohackernews.app.ui.list.view.StoryItemView
import io.dwak.holohackernews.app.ui.list.view.StoryListView

@ViewScope
@Module
open class PresenterModule(val view : PresenterView) {
    @Provides
    fun providesStoryListPresenter(serviceComponent : ServiceComponent) = getStoryListPresenter(serviceComponent)
    open fun getStoryListPresenter(serviceComponent : ServiceComponent) : StoryListPresenter = StoryListPresenterImpl(view as StoryListView, serviceComponent)

    @Provides
    fun providesStoryItemPresenter(serviceComponent : ServiceComponent) = getStoryItemPresenter(serviceComponent)
    open fun getStoryItemPresenter(serviceComponent : ServiceComponent) : StoryItemPresenter = StoryItemPresenterImpl(view as StoryItemView, serviceComponent)
}