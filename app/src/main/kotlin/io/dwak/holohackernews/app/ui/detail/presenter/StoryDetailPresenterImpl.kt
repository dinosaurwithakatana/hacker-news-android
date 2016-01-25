package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.network.HackerNewsService
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailView
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StoryDetailPresenterImpl(view : StoryDetailView, interactorComponent : InteractorComponent)
: AbstractPresenter<StoryDetailView>(view, interactorComponent), StoryDetailPresenter {
    lateinit var hackerNewsService : HackerNewsService @Inject set
    override var itemId : Long? = null

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
                    .doOnNext { view.displayStoryHeader(it) }
                    .map { it.comments }
                    .map {
                        val expandedComments = arrayListOf<CommentJson>()
                        it?.forEach {
                            expandComments(it, expandedComments)
                        }
                        expandedComments
                    }
                    .subscribe({ view.displayComments(it) },
                            { Timber.e(it, "Detail request failed for item $itemId") },
                            { view.refreshing?.call(false) })
        }
    }

    fun expandComments(comment : CommentJson, expandedComments : MutableList<CommentJson>){
        expandedComments.add(comment)

        if(comment.comments?.size == 0){
            return
        }

        comment.comments?.forEach { expandComments(it, expandedComments) }

    }
}