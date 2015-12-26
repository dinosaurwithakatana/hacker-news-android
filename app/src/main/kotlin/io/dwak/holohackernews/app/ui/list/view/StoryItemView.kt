package io.dwak.holohackernews.app.ui.list.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.json.StoryJson
import rx.Observable

interface StoryItemView : PresenterView{
    var itemClicks : Observable<Unit>?
    var saveClicks : Observable<Unit>?
    var onItemClick : ((StoryJson) -> Unit)?
    var onSaveClick : ((Boolean) -> Unit)?
    fun displayStoryDetails(title : String?,
                            points : String?,
                            domain : String?,
                            longAgo : String?,
                            commentsCount : String?,
                            submittedBy : String?)
}