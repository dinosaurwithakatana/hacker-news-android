package io.dwak.holohackernews.app.ui.detail.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.extension.PanelEvent
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import rx.Observable
import rx.functions.Action1

interface StoryDetailView : PresenterView {
  var listClicks : Observable<Unit>?
  var refreshing : Action1<in Boolean>?
  var refreshes : Observable<Unit>?
  var buttonBarText : Action1<in CharSequence>?
  var buttonBarMainActionClicks : Observable<Unit>?
  var buttonBarLeftActionClicks : Observable<Unit>?
  var buttonBarRightActionClicks : Observable<Unit>?
  var panelEvents : Observable<PanelEvent>?
  var topItem : Observable<Int>?

  fun displayStoryHeader(storyDetail : StoryDetailJson)
  fun displayComments(comments : Observable<CommentJson>)
  fun setLinkDrawerState(open : Boolean)
  fun loadLink(url : String, useExternalBrowser : Boolean)
  fun enableLinkDrawer(enable : Boolean)
  fun navigateUp(index : Int)
  fun navigateDown(index : Int)
  fun webViewBack();
  fun webViewForward();
  fun clear()
  fun setTitle(title : String?)
}