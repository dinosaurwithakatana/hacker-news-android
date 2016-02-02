package io.dwak.holohackernews.app.ui.detail.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
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
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.extension.*
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.detail.presenter.StoryDetailPresenter
import io.dwak.holohackernews.app.widget.ObservableWebView
import io.dwak.holohackernews.app.widget.RxWebChromeClient
import rx.Observable
import rx.functions.Action1

class StoryDetailFragment
: MvpFragment<StoryDetailPresenter>(),
  StoryDetailView,
  ObservableWebView.OnScrollChangedCallback {
    override var buttonBarText : Action1<in CharSequence>? = null
    override var listClicks: Observable<Unit>? = null
    override var refreshing: Action1<in Boolean>? = null
    override var refreshes: Observable<Unit>? = null
    override var buttonBarMainActionClicks : Observable<Unit>? = null
    override var panelEvents : Observable<PanelEvent>? = null
    override var headerScrolled : Observable<Boolean>? = null

    private val webProgressBar : ProgressBar by bindView(R.id.web_progress_bar)
    private val linkPanel : SlidingUpPanelLayout by bindView(R.id.link_panel)
    private val readabilityButton : FloatingActionButton by bindView(R.id.fabbutton)
    private val buttonBar : View by bindView(R.id.button_bar)
    private val buttonBarMainAction : Button by bindView(R.id.action_main)
    private val storyWebView : ObservableWebView by bindView(R.id.story_web_view)
    private val swipeRefresh : SwipeRefreshLayout by bindView(R.id.swipe_container)
    private val recycler : RecyclerView by bindView(R.id.comments_recycler)
    private var adapter : StoryDetailAdapter? = null

    companion object {
        val ITEM_ID = "ITEM_ID"
        fun newInstance(itemId : Long) : StoryDetailFragment {
            return StoryDetailFragment().with {
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
        buttonBarText = buttonBarMainAction.text()
        buttonBarMainActionClicks = buttonBarMainAction.clicks()
        panelEvents = linkPanel.panelSlides()

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        setupLinkPanel()

        adapter = StoryDetailAdapter(activity)
        recycler.addItemDecoration(CommentItemDecoration())
        recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recycler.layoutManager = layoutManager
        headerScrolled = recycler.scrollEvents().map { layoutManager.findFirstVisibleItemPosition() == 0 }

        presenter.getStoryDetails()
    }

    private fun setupLinkPanel() {
        linkPanel.setDragView(buttonBar)
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
        storyWebView.setWebViewClient(object: WebViewClient(){
            override fun onPageFinished(view : WebView?, url : String?) {
                super.onPageFinished(view, url)
            }
        })
        val chromeClient = RxWebChromeClient()
        chromeClient.progress().subscribe(webProgressBar.progress())
        storyWebView.setWebChromeClient(chromeClient)
        storyWebView.onScrollChangedCallback = this
    }

    override fun setLinkDrawerState(open : Boolean) {
        linkPanel.panelState =
                if (open) SlidingUpPanelLayout.PanelState.EXPANDED
                else SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    override fun loadLink(url : String, useExternalBrowser : Boolean) {
        when (useExternalBrowser) {
            false -> storyWebView.loadUrl(url)
            true  -> startActivity(Intent().with {
                setAction(Intent.ACTION_VIEW)
                setData(Uri.parse(url))
            })
        }
    }

    override fun disableLinkDrawer() {
        linkPanel.isTouchEnabled = false
    }

    override fun navigateUp() {
        throw UnsupportedOperationException()
    }

    override fun navigateDown() {
        throw UnsupportedOperationException()
    }

    override fun displayComments(comments : List<CommentJson>) {
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

    override fun onScroll(l : Int, t : Int, oldL : Int, oldT : Int) {
        if(t >= oldT) {
            readabilityButton.hide()
        }
        else {
            readabilityButton.show()
        }
    }
}