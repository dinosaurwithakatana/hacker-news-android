package io.dwak.holohackernews.app.ui.detail.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.jakewharton.rxbinding.support.v4.widget.refreshing
import com.jakewharton.rxbinding.support.v7.widget.scrollEvents
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.progress
import com.jakewharton.rxbinding.widget.text
import com.melnykov.fab.FloatingActionButton
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.fragment.MvpFragment
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.extension.*
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenter
import io.dwak.holohackernews.app.widget.ObservableWebView
import io.dwak.holohackernews.app.widget.RxWebChromeClient
import rx.Observable
import rx.functions.Action1
import timber.log.Timber

class StoryDetailFragment
: MvpFragment<StoryDetailPresenter>(),
  StoryDetailView {
    override var buttonBarText : Action1<in CharSequence>? = null
    override var listClicks: Observable<Unit>? = null
    override var refreshing: Action1<in Boolean>? = null
    override var refreshes: Observable<Unit>? = null
    override var buttonBarMainActionClicks : Observable<Unit>? = null
    override var buttonBarLeftActionClicks : Observable<Unit>? = null
    override var buttonBarRightActionClicks : Observable<Unit>? = null
    override var panelEvents : Observable<PanelEvent>? = null
    override var topItem : Observable<Int>? = null

    private val webProgressBar : ProgressBar by bindView(R.id.web_progress_bar)
    private val linkPanel : SlidingUpPanelLayout by bindView(R.id.link_panel)
    private val readabilityButton : FloatingActionButton by bindView(R.id.fabbutton)
    private val buttonBar : View by bindView(R.id.button_bar)
    private val buttonBarMainAction : TextView by bindView(R.id.action_main)
    private val buttonBarLeftAction : TextView by bindView(R.id.action_1)
    private val buttonBarRightAction : TextView by bindView(R.id.action_2)
    private val storyWebView : ObservableWebView by bindView(R.id.story_web_view)
    private val swipeRefresh : SwipeRefreshLayout by bindView(R.id.swipe_container)
    private val recycler : RecyclerView by bindView(R.id.comments_recycler)
    private var adapter : StoryDetailAdapter? = null
    private val recyclerState = RecyclerView.State()

    companion object {
        val ITEM_ID = "ITEM_ID"
        fun newInstance(itemId : Long) : StoryDetailFragment {
            return StoryDetailFragment().with {
                putLong(ITEM_ID, itemId)
            }
        }
    }

    override fun inject() = objectGraph(this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.itemId = getLong(ITEM_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_story_comments, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshing = swipeRefresh.refreshing()
        refreshes = swipeRefresh.refreshes()
        buttonBarText = buttonBarMainAction.text()
        buttonBarMainActionClicks = buttonBarMainAction.clicks()
        buttonBarLeftActionClicks = buttonBarLeftAction.clicks()
        buttonBarRightActionClicks = buttonBarRightAction.clicks()
        panelEvents = linkPanel.panelSlides().map { it.event }

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        setupLinkPanel()

        adapter = StoryDetailAdapter(activity)
        recycler.addItemDecoration(CommentItemDecoration())
        recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recycler.layoutManager = layoutManager
        topItem = recycler.scrollEvents()
                .map { layoutManager.findFirstVisibleItemPosition() }

        presenter.getStoryDetails()
    }

    private fun setupLinkPanel() {
        linkPanel.setDragView(buttonBarMainAction)
        webProgressBar.visibility = View.VISIBLE
        webProgressBar.max = 100
        with(storyWebView.settings){
            loadWithOverviewMode = true
            useWideViewPort = true
            supportZoom = true
            builtInZoomControls = true
            displayZoomControls = false
            javaScriptEnabled = true
        }
        val chromeClient = RxWebChromeClient()
        chromeClient.progress().subscribe(webProgressBar.progress())
        val webViewClient = object: WebViewClient(){
            override fun shouldOverrideUrlLoading(view : WebView?, url : String?) = true
        }
        storyWebView.setWebChromeClient(chromeClient)
        storyWebView.setWebViewClient(webViewClient)
        storyWebView.scrolls().map { it.t <= it.oldT }.subscribe(readabilityButton.visibility())
    }

    override fun setLinkDrawerState(open : Boolean) {
        if (open) {
            linkPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            buttonBarLeftAction.setBackgroundResource(R.drawable.ic_arrow_back_24dp)
            buttonBarRightAction.setBackgroundResource(R.drawable.ic_arrow_forward_24dp)
        }
        else {
            linkPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            buttonBarLeftAction.setBackgroundResource(R.drawable.ic_action_navigate_up_24dp)
            buttonBarRightAction.setBackgroundResource(R.drawable.ic_action_navigate_down_24dp)
        }
    }

    override fun loadLink(url : String, useExternalBrowser : Boolean) {
        Timber.d("woidhaodhw")
        when (useExternalBrowser) {
            false -> storyWebView.loadUrl(url)
            true  -> activity.viewInExternalBrowser(url)
        }
    }

    override fun disableLinkDrawer() {
        linkPanel.isTouchEnabled = false
    }

    override fun navigateUp(index : Int) {
        (recycler.layoutManager as LinearLayoutManager)
                .smoothScrollToPositionToTop(recycler, recyclerState, index)
    }

    override fun navigateDown(index : Int) {
        Timber.d(index.toString())
        (recycler.layoutManager as LinearLayoutManager)
                .smoothScrollToPositionToTop(recycler, recyclerState, index)
    }

    override fun displayComments(comments : Observable<CommentJson>) {
        adapter?.addComments(comments)
    }

    override fun displayStoryHeader(storyDetail : StoryDetailJson) {
        adapter?.addHeader(storyDetail)
    }

    override fun clear() {
        adapter?.clear()
    }

    override fun setTitle(title : String?) {
        activity.title = title
    }
}