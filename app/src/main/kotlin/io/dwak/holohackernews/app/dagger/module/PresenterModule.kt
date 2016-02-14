package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.dagger.scope.ViewScope
import io.dwak.holohackernews.app.ui.about.presenter.AboutPresenter
import io.dwak.holohackernews.app.ui.about.presenter.AboutPresenterImpl
import io.dwak.holohackernews.app.ui.about.view.AboutView
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenter
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenterImpl
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenter
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenterImpl
import io.dwak.holohackernews.app.ui.detail.view.CommentView
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailView
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import io.dwak.holohackernews.app.ui.list.view.StoryItemView
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenterImpl
import io.dwak.holohackernews.app.ui.main.view.MainView
import io.dwak.holohackernews.app.ui.navigation.presenter.NavigationDrawerPresenter
import io.dwak.holohackernews.app.ui.navigation.presenter.NavigationDrawerPresenterImpl
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView

@ViewScope
@Module
open class PresenterModule(private val view : PresenterView) {
    @Provides
    fun storyListPresenter(interactorComponent : InteractorComponent) : StoryListPresenter
            = StoryListPresenterImpl(view as StoryListView, interactorComponent)

    @Provides
    fun storyItemPresenter(interactorComponent : InteractorComponent) : StoryItemPresenter
            = StoryItemPresenterImpl(view as StoryItemView, interactorComponent)

    @Provides
    fun mainPresenter(interactorComponent : InteractorComponent) : MainPresenter
            = MainPresenterImpl(view as MainView, interactorComponent)

    @Provides
    fun navigationPresenter(interactorComponent : InteractorComponent) : NavigationDrawerPresenter
            = NavigationDrawerPresenterImpl(view as NavigationDrawerView, interactorComponent)

    @Provides
    fun aboutPresenter(interactorComponent : InteractorComponent) : AboutPresenter
            = AboutPresenterImpl(view as AboutView, interactorComponent)

    @Provides
    fun storyDetailPresenter(interactorComponent : InteractorComponent) : StoryDetailPresenter
            = StoryDetailPresenterImpl(view as StoryDetailView, interactorComponent)

    @Provides
    fun commentPresenter(interactorComponent : InteractorComponent) : CommentPresenter
            = CommentPresenterImpl(view as CommentView, interactorComponent)
}