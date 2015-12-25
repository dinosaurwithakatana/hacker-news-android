package io.dwak.holohackernews.app.ui.list.view

import io.dwak.holohackernews.app.base.mvp.PresenterView

interface StoryItemView : PresenterView{
    fun displayStoryDetails(title : String?,
                            points : String?,
                            domain : String?,
                            longAgo : String?,
                            commentsCount : String?,
                            submittedBy : String?)
}