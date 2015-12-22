package io.dwak.holohackernews.app.ui.list.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryJson

interface StoryListView : PresenterView {
    fun displayStories(storyList : List<StoryJson>)
    fun navigateToStoryDetail(itemId : Long?)
}