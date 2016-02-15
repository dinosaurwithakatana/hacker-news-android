package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.model.json.CommentJson

interface CommentPresenter : Presenter {
  var isOriginalPoster : Boolean?
  var comment : CommentJson?
  var isCollapsed : Boolean

  fun commentClicked()
}