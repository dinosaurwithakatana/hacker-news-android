package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.model.json.StoryJson
import rx.Single

interface StoryItemPresenter : Presenter {
    var story : StoryJson?
    fun save() : Single<Boolean>
}