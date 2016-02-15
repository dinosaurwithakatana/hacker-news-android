package io.dwak.holohackernews.app.ui.detail.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.model.json.CommentJson

class CommentItemDecoration : RecyclerView.ItemDecoration() {
  override fun getItemOffsets(outRect : Rect,
                              view : View,
                              parent : RecyclerView,
                              state : RecyclerView.State) {
    super.getItemOffsets(outRect, view, parent, state)
    val adapter = parent.adapter as StoryDetailAdapter?
    val position = parent.getChildAdapterPosition(view)
    if (adapter?.itemList?.get(position)?.viewType == StoryDetailAdapter.ViewType.HEADER) return
    val model = adapter?.itemList?.get(position)?.value as CommentJson
    outRect.left = view.context.resources.getDimension(R.dimen.color_code_left_margin).toInt() * model.level!!
  }
}