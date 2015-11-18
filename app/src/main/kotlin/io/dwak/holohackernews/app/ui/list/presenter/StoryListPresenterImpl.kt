package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.ServiceComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class StoryListPresenterImpl(view : StoryListView, serviceComponent : ServiceComponent)
: AbstractPresenter<StoryListView>(view, serviceComponent), StoryListPresenter {
    override var feed : Feed? = null
        set(value) {
            field = value
            getStoryObservable()
        }

    override fun inject() = serviceComponent.inject(this)

    private fun getStoryObservable() {
        var storyListObservable : Observable<List<StoryJson>>? = null
        feed?.let {
            with(hackerNewsService){
                when(it) {
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
                ?.observeOn(Schedulers.io())
                ?.subscribeOn(AndroidSchedulers.mainThread())
                ?.subscribe { view.displayStories(it) }
    }

    override fun storyClicked(story : StoryJson) = view.navigateToStoryDetail(story.id)

    override fun loadPageTwo() {
        feed.let {
            if(it == Feed.TOP){
                hackerNewsService.getTopStoriesPageTwo()
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe { view.displayStories(it) }
            }
        }
    }

}