package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson

interface StoryListPresenter : Presenter {
    var feed : Feed?

    fun storyClicked(story : StoryJson)

    fun loadPageTwo()
}