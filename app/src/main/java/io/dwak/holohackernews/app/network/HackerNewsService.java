package io.dwak.holohackernews.app.network;

import java.util.List;

import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Retrofit service to interface with hacker news api
 * Created by vishnu on 4/21/14.
 */
public interface HackerNewsService {
    @GET("/news")
    void getTopStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/news2")
    void getTopStoriesPageTwo(Callback<List<NodeHNAPIStory>> callback);

    @GET("/newest")
    void getNewestStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/best")
    void getBestStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/item/{itemId}")
    void getItemDetails(@Path("itemId") long itemId, Callback<NodeHNAPIStoryDetail> callback);
}
