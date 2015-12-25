package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
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
    fun providesStoryListPresenter(interactorComponent: InteractorComponent)
            = getStoryListPresenter(interactorComponent)
    open fun getStoryListPresenter(interactorComponent: InteractorComponent) : StoryListPresenter
            = StoryListPresenterImpl(view as StoryListView, interactorComponent)

    @Provides
    fun providesStoryItemPresenter(interactorComponent: InteractorComponent)
            = getStoryItemPresenter(interactorComponent)
    open fun getStoryItemPresenter(interactorComponent: InteractorComponent) : StoryItemPresenter
            = StoryItemPresenterImpl(view as StoryItemView, interactorComponent)

    @Provides
    fun providesMainPresenter(interactorComponent: InteractorComponent)
            = getMainPresenter(interactorComponent)
    open fun getMainPresenter(interactorComponent: InteractorComponent) : MainPresenter
            = MainPresenterImpl(view as MainView, interactorComponent)
}