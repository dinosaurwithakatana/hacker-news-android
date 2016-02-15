package io.dwak.holohackernews.app.dagger.component

import android.content.res.Resources
import dagger.Component
import io.dwak.holohackernews.app.dagger.interactor.HtmlInteractor
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.NetworkModule
import io.dwak.holohackernews.app.ui.about.presenter.AboutPresenterImpl
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenterImpl
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenterImpl
import io.dwak.holohackernews.app.ui.navigation.presenter.NavigationDrawerPresenterImpl
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class, InteractorModule::class))
interface InteractorComponent {
  fun inject(storyListPresenterImpl : StoryListPresenterImpl)
  fun inject(storyItemPresenterImpl : StoryItemPresenterImpl)
  fun inject(mainPresenter : MainPresenterImpl)
  fun inject(navigationDrawerPresenterImpl : NavigationDrawerPresenterImpl)
  fun inject(aboutPresenterImpl : AboutPresenterImpl)
  fun inject(storyDetailPresenterImpl : StoryDetailPresenterImpl)
  fun inject(commentPresenterImpl : CommentPresenterImpl)

  val rxSchedulerInteractor : RxSchedulerInteractor
  val resources : Resources
  val htmlParser : HtmlInteractor.HtmlParser
}