package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.BasePresenterTest
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.view.StoryListView
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable
import java.util.*

class StoryListPresenterImplTest : BasePresenterTest<StoryListPresenter>() {
    internal val view = mock(StoryListView::class.java)

    @Test
    @Throws(Exception::class)
    fun testSetFeed() {
        val testNetworkModule = TestNetworkModule()
        val storyJsons = ArrayList<StoryJson>()
        storyJsons.add(StoryJson(1L, "title", "url", "domain", 10, "user", "timeAgo", 3, "Job"))
        `when`(testNetworkModule.mockedService.getTopStories()).thenReturn(Observable.just(storyJsons))
        `when`(view.refreshes).thenReturn(Observable.empty())
        getComponent(view, testNetworkModule).inject(this)
        presenter.feed = Feed.TOP
        presenter.onAttachToView()
        presenter.getStories()
        verify(view).addStories(Feed.TOP.titleRes, storyJsons)
        verify(view).clearStories()
    }

    @Test
    @Throws(Exception::class)
    fun testOnStoryClicked() {
        val storyJson = StoryJson(1L, "title", "url", "domain", 10, "user", "timeAgo", 3, "Job")
        `when`(view.refreshes).thenReturn(Observable.empty())
        getComponent(view, TestNetworkModule()).inject(this)
        presenter.onAttachToView()
        presenter.storyClicked(storyJson)
        verify(view).navigateToStoryDetail(storyJson.id)
    }
}
