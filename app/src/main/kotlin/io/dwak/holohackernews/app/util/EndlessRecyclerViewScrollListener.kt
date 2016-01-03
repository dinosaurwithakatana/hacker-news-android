package io.dwak.holohackernews.app.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlin.properties.Delegates

class EndlessRecyclerViewScrollListener(private val layoutManager : LinearLayoutManager,
                                        private val onLoadMore : (Int) -> Unit)
: RecyclerView.OnScrollListener() {
    private var loading = false
    private var previousTotal = 0
    private var visibleThreshold = 5
    private var currentPage = 1
    private var firstVisibleItem : Int by Delegates.notNull()
    private var visibleItemCount : Int by Delegates.notNull()
    private var totalItemCount : Int by Delegates.notNull()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        when {
            loading -> if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
            else -> if (totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                currentPage++

                onLoadMore(currentPage)
                loading = true
            }
        }
    }
}