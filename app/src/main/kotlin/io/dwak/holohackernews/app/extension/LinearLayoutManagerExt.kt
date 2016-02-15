package io.dwak.holohackernews.app.extension

import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView

fun LinearLayoutManager.smoothScrollToPositionToTop(recyclerView : RecyclerView,
                                                    state : RecyclerView.State,
                                                    index : Int) {


  val self = this
  val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
    override fun getVerticalSnapPreference() : Int {
      return SNAP_TO_START;
    }

    override fun computeScrollVectorForPosition(targetPosition : Int) : PointF? {
      return self.computeScrollVectorForPosition(targetPosition);
    }
  }

  linearSmoothScroller.targetPosition = index;
  startSmoothScroll(linearSmoothScroller);
}

