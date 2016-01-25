package io.dwak.holohackernews.app.ui.detail.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.jakewharton.rxbinding.support.v4.widget.refreshing
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.fragment.MvpFragment
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.extension.getLong
import io.dwak.holohackernews.app.extension.withArgs
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenter
import rx.Observable
import rx.functions.Action1

class StoryDetailFragment : MvpFragment<StoryDetailPresenter>(), StoryDetailView{
    override val listClicks: Observable<Unit>
        get() = throw UnsupportedOperationException()
    override var refreshing: Action1<in Boolean>? = null
    override var refreshes: Observable<Unit>? = null

    private val linkPanel : SlidingUpPanelLayout by bindView(R.id.link_panel)
    private val swipeRefresh : SwipeRefreshLayout by bindView(R.id.swipe_container)
    private val recycler : RecyclerView by bindView(R.id.comments_recycler)
    private var adapter : StoryDetailAdapter? = null

    companion object {
        val ITEM_ID = "ITEM_ID"
        fun newInstance(itemId : Long) : StoryDetailFragment {
            return StoryDetailFragment().withArgs {
                putLong(ITEM_ID, itemId)
            }
        }
    }

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(activity))
                        .build())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.itemId = getLong(ITEM_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_story_comments, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshing = swipeRefresh.refreshing()
        refreshes = swipeRefresh.refreshes()
        adapter = StoryDetailAdapter(activity)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity)
        presenter.getStoryDetails()
    }

    override fun openLinkDrawer() {
        throw UnsupportedOperationException()
    }

    override fun navigateUp() {
        throw UnsupportedOperationException()
    }

    override fun navigateDown() {
        throw UnsupportedOperationException()
    }

    override fun displayComments(comments : List<CommentJson>) {
    }

    override fun displayStoryHeader(storyDetail : StoryDetailJson) {
        adapter?.addHeader(storyDetail)
    }

}