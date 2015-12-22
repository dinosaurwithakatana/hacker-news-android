package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.NetworkComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.network.HackerNewsService2
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class StoryListPresenterImpl(view: StoryListView, networkComponent: NetworkComponent)
: AbstractPresenter<StoryListView>(view, networkComponent), StoryListPresenter {
    lateinit var hackerNewsService: HackerNewsService2 @Inject set

    override var feed: Feed? = null
        set(value) {
            field = value
            getStoryObservable()
        }

    override fun inject() = networkComponent.inject(this)

    private fun getStoryObservable() {
        var storyListObservable: Observable<MutableList<StoryJson>>? = null
        feed?.let {
            with(hackerNewsService) {
                when (it) {
                    Feed.TOP -> storyListObservable = getTopStories()
                    Feed.BEST -> storyListObservable = getBestStories()
                    Feed.NEW -> storyListObservable = getNewestStories()
                    Feed.SHOW -> storyListObservable = getShowHnStories()
                    Feed.NEW_SHOW -> storyListObservable = getNewShowHnStories()
                    Feed.ASK -> storyListObservable = getAskHnStories()
                    else -> {}
                }
            }
        }

        storyListObservable
                ?.observeOn(networkComponent.rxSchedulerInteractor.mainThreadScheduler)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({ view.displayStories(it) })
    }

    override fun storyClicked(story: StoryJson) = view.navigateToStoryDetail(story.id)

    override fun loadPageTwo() {
        feed.let {
            if (it == Feed.TOP) {
                hackerNewsService.getTopStoriesPageTwo()
                        .observeOn(networkComponent.rxSchedulerInteractor.mainThreadScheduler)
                        .subscribeOn(Schedulers.io())
                        .subscribe { view.displayStories(it) }
            }
        }
    }

}