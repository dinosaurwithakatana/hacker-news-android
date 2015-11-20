package io.dwak.holohackernews.app.dagger.module

import android.view.ViewStructure
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.base.base.mvp.PresenterView
import io.dwak.holohackernews.app.dagger.component.NetworkComponent
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
    fun providesStoryListPresenter(networkComponent: NetworkComponent) = getStoryListPresenter(networkComponent)
    open fun getStoryListPresenter(networkComponent: NetworkComponent) : StoryListPresenter = StoryListPresenterImpl(view as StoryListView, networkComponent)

    @Provides
    fun providesStoryItemPresenter(networkComponent: NetworkComponent) = getStoryItemPresenter(networkComponent)
    open fun getStoryItemPresenter(networkComponent: NetworkComponent) : StoryItemPresenter = StoryItemPresenterImpl(view as StoryItemView, networkComponent)
}