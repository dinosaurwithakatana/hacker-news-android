package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import rx.Observable

interface CommentView : PresenterView {
  var commentClicks : Observable<StoryDetailAdapter.ClickEvent>?
  fun displayComment(content : CharSequence?,
                     submitter : String?,
                     submissionTime : String?,
                     level : Int?,
                     isOriginalPoster : Boolean?,
                     isCollapsed : Boolean)

  fun collapseChildren()
}