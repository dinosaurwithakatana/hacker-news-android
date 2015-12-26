package io.dwak.holohackernews.app.ui.list.view

import android.support.annotation.StringRes
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryJson

interface StoryListView : PresenterView {
    fun displayStories(@StringRes titleRes : Int,
                       storyList : List<StoryJson>)
    fun navigateToStoryDetail(itemId : Long?)
}