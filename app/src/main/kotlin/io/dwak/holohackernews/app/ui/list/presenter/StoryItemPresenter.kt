package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.model.json.StoryJson
import rx.Single

interface StoryItemPresenter : Presenter {
    var story : StoryJson?
    var storyDetail : StoryDetailJson?
    fun save() : Single<Boolean>
}