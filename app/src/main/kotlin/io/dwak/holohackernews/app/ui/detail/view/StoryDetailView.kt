package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import rx.Observable
import rx.functions.Action1

interface StoryDetailView : PresenterView {
    val listClicks : Observable<Unit>
    var refreshing : Action1<in Boolean>?
    var refreshes : Observable<Unit>?

    fun displayStory(storyDetail : StoryDetailJson)
    fun openLinkDrawer()
    fun navigateUp()
    fun navigateDown()
}