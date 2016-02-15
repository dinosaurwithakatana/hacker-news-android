package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.ui.detail.view.CommentView

class CommentPresenterImpl(view : CommentView, interactorComponent : InteractorComponent)
: AbstractPresenter<CommentView>(view, interactorComponent), CommentPresenter {
  override var isCollapsed : Boolean = false
  override var isOriginalPoster : Boolean? = null
  override var comment : CommentJson? = null
    set(value) {
      field = value
      parseComment()
    }

  override fun inject() = interactorComponent.inject(this)

  fun parseComment() {
    if (comment?.content != null) {
      view.displayComment(content = interactorComponent.htmlParser.fromHtml(comment?.content),
                          submitter = comment?.user,
                          submissionTime = comment?.timeAgo,
                          isOriginalPoster = isOriginalPoster,
                          level = comment?.level,
                          isCollapsed = isCollapsed)
    }
  }

  override fun commentClicked() {
    throw UnsupportedOperationException()
  }

}