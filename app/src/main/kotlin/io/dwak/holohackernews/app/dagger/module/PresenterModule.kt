package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.dagger.component.NetworkComponent
import io.dwak.holohackernews.app.dagger.scope.ViewScope
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import io.dwak.holohackernews.app.ui.list.view.StoryItemView
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenterImpl
import io.dwak.holohackernews.app.ui.main.view.MainView

@ViewScope
@Module
open class PresenterModule(val view : PresenterView) {
    @Provides
    fun providesStoryListPresenter(networkComponent: NetworkComponent)
            = getStoryListPresenter(networkComponent)
    open fun getStoryListPresenter(networkComponent: NetworkComponent) : StoryListPresenter
            = StoryListPresenterImpl(view as StoryListView, networkComponent)

    @Provides
    fun providesStoryItemPresenter(networkComponent: NetworkComponent)
            = getStoryItemPresenter(networkComponent)
    open fun getStoryItemPresenter(networkComponent: NetworkComponent) : StoryItemPresenter
            = StoryItemPresenterImpl(view as StoryItemView, networkComponent)

    @Provides
    fun providesMainPresenter(networkComponent: NetworkComponent)
            = getMainPresenter(networkComponent)
    open fun getMainPresenter(networkComponent: NetworkComponent) : MainPresenter
            = MainPresenterImpl(view as MainView, networkComponent)
}