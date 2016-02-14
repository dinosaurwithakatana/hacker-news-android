package io.dwak.holohackernews.app.ui.list.view

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.jakewharton.rxbinding.support.v4.widget.refreshing
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.fragment.MvpFragment
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.extension.bindActivity
import io.dwak.holohackernews.app.extension.getSerializable
import io.dwak.holohackernews.app.extension.with
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter
import io.dwak.holohackernews.app.util.EndlessRecyclerViewScrollListener
import rx.Observable
import rx.functions.Action1
import timber.log.Timber

class StoryListFragment : MvpFragment<StoryListPresenter>(), StoryListView {
    override var refreshing : Action1<in Boolean>? = null
    override var refreshes : Observable<Unit>? = null
    private val storyList : RecyclerView by bindView(R.id.story_list)
    private val swipeRefresh : SwipeRefreshLayout by bindView(R.id.swipe_container)
    private val listener : StoryListInteractionListener by bindActivity()
    private var adapter : StoryListAdapter? = null

    companion object {
        val FEED_ARG = "FEED"
        fun newInstance(feed : Feed) = StoryListFragment().with {
            putSerializable(FEED_ARG, feed)
        }
    }

    override fun inject() = objectGraph(this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.feed = getSerializable(FEED_ARG)
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        return inflater!!.inflate(R.layout.fragment_storylist_list, container, false)
    }

    override fun onViewCreated(view : View?, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshing = swipeRefresh.refreshing()
        refreshes = swipeRefresh.refreshes()
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        adapter = StoryListAdapter(activity)
        adapter?.onItemClicked = { presenter.storyClicked(it) }
        storyList.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        storyList.layoutManager = layoutManager
        storyList.addOnScrollListener(EndlessRecyclerViewScrollListener(layoutManager, { presenter.loadPageTwo() }))
        presenter.getStories()
    }

    override fun addStories(@StringRes titleRes: Int,
                            storyList: List<StoryJson>) {
        activity.setTitle(titleRes)
        storyList.forEach { adapter?.addStory(it) }
    }

    override fun navigateToStoryDetail(itemId : Long) {
        Timber.d("navigateToStoryDetail : $itemId")
        listener.navigateToStoryDetail(itemId)
    }

    override fun clearStories() {
        activity.title = ""
        adapter?.clear()
    }

    interface StoryListInteractionListener {
        fun navigateToStoryDetail(itemId: Long)
    }
}