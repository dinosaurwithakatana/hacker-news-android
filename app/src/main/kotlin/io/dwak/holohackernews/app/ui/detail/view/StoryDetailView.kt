package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import rx.Observable

interface StoryDetailView : PresenterView {
    val listClicks : Observable<Unit>

    fun displayStory(storyDetail : StoryDetailJson)
    fun openLinkDrawer()
    fun navigateUp()
    fun navigateDown()
}