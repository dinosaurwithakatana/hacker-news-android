package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.network.HackerNewsService
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class StoryListPresenterImpl(view: StoryListView, interactorComponent: InteractorComponent)
: AbstractPresenter<StoryListView>(view, interactorComponent), StoryListPresenter {
    override var feed: Feed? = null
    lateinit var hackerNewsService: HackerNewsService @Inject set
    private var pageTwoLoaded = false


    override fun inject() = interactorComponent.inject(this)

    override fun onAttachToView() {
        super.onAttachToView()
        with(viewSubscription){
            add(view.refreshes?.subscribe { getStories() })
        }
    }

    override fun getStories() {
        view.clearStories()
        view.refreshing?.call(true)
        var storyListObservable: Observable<MutableList<StoryJson>>? = null
        feed?.let {
            f: Feed ->
            with(hackerNewsService) {
                when (f) {
                    Feed.TOP -> storyListObservable = getTopStories()
                    Feed.BEST -> storyListObservable = getBestStories()
                    Feed.NEW -> storyListObservable = getNewestStories()
                    Feed.SHOW -> storyListObservable = getShowHnStories()
                    Feed.NEW_SHOW -> storyListObservable = getNewShowHnStories()
                    Feed.ASK -> storyListObservable = getAskHnStories()
                    else -> {}
                }
            }
            storyListObservable
                    ?.observeOn(interactorComponent.rxSchedulerInteractor.mainThreadScheduler)
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe(
                            { view.addStories(f.titleRes, it) },
                            {},
                            { view.refreshing?.call(false) }
                    )
        }

    }

    override fun storyClicked(story: StoryJson) = view.navigateToStoryDetail(story.id)

    override fun loadPageTwo() {
        if(feed == Feed.TOP && !pageTwoLoaded) {
            feed?.let {
                val f = it
                if (f == Feed.TOP) {
                    hackerNewsService.getTopStoriesPageTwo()
                            .observeOn(interactorComponent.rxSchedulerInteractor.mainThreadScheduler)
                            .subscribeOn(Schedulers.io())
                            .subscribe { view.addStories(f.titleRes, it) }
                }
            }
        }
        else {
            pageTwoLoaded = true
        }
    }

}