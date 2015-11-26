package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.dagger.interactor.RxSchedulerInteractor
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.NetworkModule
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenterImpl
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenterImpl
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenterImpl
import rx.Scheduler
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class, InteractorModule::class))
interface NetworkComponent {
    fun inject(storyListPresenterImpl : StoryListPresenterImpl)
    fun inject(storyItemPresenterImpl : StoryItemPresenterImpl)
    fun inject(mainPresenter: MainPresenterImpl)

    val rxSchedulerInteractor : RxSchedulerInteractor
}