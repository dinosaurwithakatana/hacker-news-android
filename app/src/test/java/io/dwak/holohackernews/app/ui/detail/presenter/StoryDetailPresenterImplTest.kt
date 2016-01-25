package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.BasePresenterTest
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailView
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable
import rx.functions.Action1

class StoryDetailPresenterImplTest : BasePresenterTest<StoryDetailPresenter>() {
    val view = mock(StoryDetailView::class.java)

    @Test
    fun testSetHackerNewsService() {
        val networkModule = TestNetworkModule()
        //create a level 2 comment to be nested
        val level2Comment3 = CommentJson(id = 3, level = 2, comments = null,
                content = null, timeAgo = null, user = null)
        //create a level 1 comment with the previous level 2 comment nested
        val level1Comment1 = CommentJson(id = 1, level = 1, comments = arrayListOf(level2Comment3),
                content = null, timeAgo = null, user = null)
        //create a level 1 comment with no children
        val level1Comment2 = CommentJson(id = 2, level = 1, comments = null,
                content = null, timeAgo = null, user = null)
        //create a parent comment with the previous two level1 comments as children
        val parentComment = CommentJson(id = 0, level = 0,
                comments = arrayListOf(level1Comment1, level1Comment2),
                content = null, timeAgo = null, user = null)
        val storyDetail = StoryDetailJson(id = 0, title = "title", url = "url",
                domain = "domain", points = 1, user = "user",
                timeAgo = "1d", commentsCount = 3, content = "content",
                link = "link", comments = arrayListOf(parentComment), moreCommentsId = null,
                poll = null, type = "link")

        `when`(networkModule.mockedService.getItemDetails(0))
                .thenReturn(Observable.just(storyDetail))
        `when`(view.refreshing).thenReturn(Action1 { })

        getComponent(view, networkModule).inject(this)
        with(presenter) {
            itemId = 0
            onAttachToView()
            getStoryDetails()
        }
        verify(view).displayStoryHeader(storyDetail)
        verify(view).displayComments(arrayListOf(parentComment, level1Comment1,
                level2Comment3, level1Comment2))
    }
}