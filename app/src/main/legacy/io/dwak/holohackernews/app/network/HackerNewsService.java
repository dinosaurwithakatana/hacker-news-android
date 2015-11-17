package io.dwak.holohackernews.app.network;

import java.util.List;

import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Retrofit service to interface with hacker news api
 * Created by vishnu on 4/21/14.
 */
public interface HackerNewsService {
    @GET("/news")
    void getTopStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/news")
    Observable<List<NodeHNAPIStory>> getTopStories();

    @GET("/news2")
    void getTopStoriesPageTwo(Callback<List<NodeHNAPIStory>> callback);

    @GET("/news2")
    Observable<List<NodeHNAPIStory>> getTopStoriesPageTwo();

    @GET("/newest")
    void getNewestStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/newest")
    Observable<List<NodeHNAPIStory>> getNewestStories();

    @GET("/best")
    void getBestStories(Callback<List<NodeHNAPIStory>> callback);

    @GET("/best")
    Observable<List<NodeHNAPIStory>> getBestStories();

    @GET("/show")
    Observable<List<NodeHNAPIStory>> getShowStories();

    @GET("/shownew")
    Observable<List<NodeHNAPIStory>> getShowNewStories();

    @GET("/ask")
    Observable<List<NodeHNAPIStory>> getAskStories();

    @GET("/item/{itemId}")
    void getItemDetails(@Path("itemId") long itemId, Callback<NodeHNAPIStoryDetail> callback);

    @GET("/item/{itemId}")
    Observable<NodeHNAPIStoryDetail> getItemDetails(@Path("itemId") long itemId);

}
