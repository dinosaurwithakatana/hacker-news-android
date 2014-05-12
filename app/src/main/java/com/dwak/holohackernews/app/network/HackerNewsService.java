package com.dwak.holohackernews.app.network;

import com.dwak.holohackernews.app.network.models.Story;
import com.dwak.holohackernews.app.network.models.StoryDetail;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

/**
 * Retrofit service to interface with hacker news api
 * Created by vishnu on 4/21/14.
 */
public interface HackerNewsService {
    @GET("/news")
    void getTopStories(Callback<List<Story>> callback);

    @GET("/news2")
    void getTopStoriesPageTwo(Callback<List<Story>> callback);

    @GET("/newest")
    void getNewestStories(Callback<List<Story>> callback);

    @GET("/best")
    void getBestStories(Callback<List<Story>> callback);

    @GET("/item/{itemId}")
    void getItemDetails(@Path("itemId") long itemId, Callback<StoryDetail> callback);
}
