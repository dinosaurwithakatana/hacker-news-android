package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.mvp.PresenterView

interface CommentView : PresenterView {
    fun displayComment(content : CharSequence?,
                       submitter : String?,
                       level : Int?,
                       isOriginalPoster : Boolean?)
}