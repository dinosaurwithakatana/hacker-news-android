package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.network.HackerNewsService
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailView
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StoryDetailPresenterImpl(view : StoryDetailView, interactorComponent: InteractorComponent)
: AbstractPresenter<StoryDetailView>(view, interactorComponent), StoryDetailPresenter{
    private lateinit var hackerNewsService : HackerNewsService @Inject set
    override var itemId: Long? = null

    override fun inject() = interactorComponent.inject(this)

    override fun onAttachToView() {
        super.onAttachToView()
        with(viewSubscription) {
        }
    }

    override fun getStoryDetails() {
        itemId?.let {
            view.refreshing?.call(true)
            hackerNewsService.getItemDetails(it)
            .observeOn(interactorComponent.rxSchedulerInteractor.mainThreadScheduler)
            .subscribeOn(Schedulers.io())
            .subscribe({view.displayStory(it)},
                    { Timber.e(it, "Detail request failed for item $itemId")},
                    {view.refreshing?.call(false)})
        }
    }
}