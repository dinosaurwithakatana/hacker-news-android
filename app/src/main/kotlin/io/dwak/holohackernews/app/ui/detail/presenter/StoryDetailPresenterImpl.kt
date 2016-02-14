package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.dagger.interactor.UserPreferenceInteractor
import io.dwak.holohackernews.app.extension.PanelEvent
import io.dwak.holohackernews.app.model.StoryType
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.network.HackerNewsService
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailView
import rx.Observable
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StoryDetailPresenterImpl(view : StoryDetailView, interactorComponent : InteractorComponent)
: AbstractPresenter<StoryDetailView>(view, interactorComponent), StoryDetailPresenter {
    lateinit var hackerNewsService : HackerNewsService @Inject set
    lateinit var userPreferences : UserPreferenceInteractor @Inject set
    override var itemId : Long? = null

    override fun inject() = interactorComponent.inject(this)

    var storyDetail : StoryDetailJson? = null
    private var linkDrawerOpen = false
    private val commentsList = arrayListOf<CommentJson>()
    private val topLevelCommentIndexes = arrayListOf<Int>()
    private var topItem : Int = 0

    override fun onAttachToView() {
        super.onAttachToView()
        with(viewSubscription) {
            add(view.refreshes
                    ?.subscribe { getStoryDetails() })

            add(view.buttonBarMainActionClicks
                    ?.doOnNext { linkDrawerOpen = !linkDrawerOpen }
                    ?.map { linkDrawerOpen }
                    ?.subscribe { setLinkDrawer(it) })

            add(view.panelEvents
                    ?.map { it == PanelEvent.EXPANDED }
                    ?.doOnNext { linkDrawerOpen = it }
                    ?.subscribe { setLinkDrawer(it) })

            add(view.topItem
                    ?.doOnNext { topItem = it }
                    ?.map { it == 0 }
                    ?.subscribe { headerScrolled(it) })

            add(view.buttonBarLeftActionClicks
                    ?.subscribe { navigateUp() })

            add(view.buttonBarRightActionClicks
                    ?.subscribe { navigateDown() })

        }

        if (userPreferences.shouldUseExternalBrowser()) {
            view.buttonBarText?.call(interactorComponent.resources.getString(R.string.open_in_browser))
        }
        else {
            view.buttonBarText?.call(interactorComponent.resources.getString(R.string.show_link))
        }
    }

    private fun navigateUp() {
        topLevelCommentIndexes.filter { it < topItem }
                .takeLast(1)
                .forEach { view.navigateUp(it) }
    }

    private fun navigateDown() {
        topLevelCommentIndexes.filter { it > topItem }
                .take(1)
                .forEach { view.navigateDown(it) }
    }

    override fun getStoryDetails() {
        itemId?.let {
            view.clear()
            view.refreshing?.call(true)

            val response = hackerNewsService.getItemDetails(it)
                    .observeOn(interactorComponent.rxSchedulerInteractor.mainThreadScheduler)
                    .subscribeOn(Schedulers.io())
                    .publish()

            response.subscribe {
                storyDetail = it
                view.displayStoryHeader(it)
            }

            response.filter { StoryType.LINK.type == it.type }
                    .map { it.url }
                    .subscribe {
                        if (it != null) {
                            view.loadLink(it, userPreferences.shouldUseExternalBrowser())
                            view.setLinkDrawerState(userPreferences.shouldShowLinkFirst())
                        }
                        else {
                            view.disableLinkDrawer()
                        }
                    }

            val comments = response.map { it.comments }
                    .map {
                        val expandedComments = arrayListOf<CommentJson>()
                        it?.forEach { expandComments(it, expandedComments) }
                        expandedComments
                    }
                    .map { Observable.from(it) }

            comments.doOnNext { it.forEach { commentsList.add(it) } }
                    .subscribe({ view.displayComments(it) },
                            { Timber.e(it, "Detail request failed for item $itemId") },
                            { view.refreshing?.call(false) })

            comments.subscribe(
                    {
                        it.filter { it.level == 0 }
                                .map { commentsList.indexOf(it) }
                                .map { it + 1 } // make sure we handle the header index at 0!
                                .subscribe {
                                    topLevelCommentIndexes.add(it)
                                }
                    },
                    {},
                    { Timber.d(topLevelCommentIndexes.toString()) })

            response.connect()
        }
    }


    override fun headerScrolled(headerVisible : Boolean) {
        if (headerVisible) {
            view.setTitle(interactorComponent.resources.getString(R.string.app_name))
        }
        else {
            view.setTitle(storyDetail?.title)
        }
    }

    private fun expandComments(comment : CommentJson, expandedComments : MutableList<CommentJson>) {
        expandedComments.add(comment)

        if (comment.comments?.size == 0) {
            return
        }

        comment.comments?.forEach { expandComments(it, expandedComments) }

    }

    private fun setLinkDrawer(open : Boolean) {
        if (open) {
            setLinkDrawerText(interactorComponent.resources.getString(R.string.show_comments))
        }
        else {
            setLinkDrawerText(interactorComponent.resources.getString(R.string.show_link))
        }

        view.setLinkDrawerState(open)
    }

    private fun setLinkDrawerText(text : String) {
        view.buttonBarText?.call(text)
    }
}