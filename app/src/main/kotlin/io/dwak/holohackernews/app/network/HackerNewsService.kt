package io.dwak.holohackernews.app.network

import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.model.json.StoryJson
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface HackerNewsService {
    @GET("/news") fun getTopStories() : Observable<MutableList<StoryJson>>
    @GET("/news2") fun getTopStoriesPageTwo() : Observable<MutableList<StoryJson>>
    @GET("/newest") fun getNewestStories() : Observable<MutableList<StoryJson>>
    @GET("/best") fun getBestStories() : Observable<MutableList<StoryJson>>
    @GET("/show") fun getShowHnStories() : Observable<MutableList<StoryJson>>
    @GET("/shownew") fun getNewShowHnStories() : Observable<MutableList<StoryJson>>
    @GET("/ask") fun getAskHnStories() : Observable<MutableList<StoryJson>>
    @GET("/item/{itemId}") fun getItemDetails(@Path(value = "itemId") itemId : Long) : Observable<StoryDetailJson>
}