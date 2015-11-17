package io.dwak.holohackernews.app.network

import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.model.json.StoryJson
import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

interface HackerNewsService2 {
    @GET("/news") fun getTopStories() : Observable<List<StoryJson>>
    @GET("/news2") fun getTopStoriesPageTwo() : Observable<List<StoryJson>>
    @GET("/newest") fun getNewestStories() : Observable<List<StoryJson>>
    @GET("/best") fun getBestStories() : Observable<List<StoryJson>>
    @GET("/show") fun getShowHnStories() : Observable<List<StoryJson>>
    @GET("/shownew") fun getNewShowHnStories() : Observable<List<StoryJson>>
    @GET("/ask") fun getAskHnStories() : Observable<List<StoryJson>>
    @GET("/item/{itemId}") fun getItemDetails(@Path(value = "itemId") itemId : Long) : Observable<StoryDetailJson>
}