package io.dwak.holohackernews.app.ui.list.view

import android.support.annotation.StringRes
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryJson
import rx.Observable
import rx.functions.Action1

interface StoryListView : PresenterView {
    var refreshing : Action1<in Boolean>?
    var refreshes : Observable<Unit>?

    fun clearStories()
    fun displayStories(@StringRes titleRes : Int,
                       storyList : List<StoryJson>)
    fun navigateToStoryDetail(itemId : Long?)
}