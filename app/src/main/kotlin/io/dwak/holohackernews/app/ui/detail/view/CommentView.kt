package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.base.mvp.PresenterView

interface CommentView : PresenterView {
    fun displayComment(content : CharSequence)
}