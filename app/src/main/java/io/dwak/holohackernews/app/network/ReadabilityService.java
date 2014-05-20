package io.dwak.holohackernews.app.network;

import io.dwak.holohackernews.app.network.models.ReadabilityArticle;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by vishnu on 5/19/14.
 */
public interface ReadabilityService {
    @GET("/parser/")
    void getReadabilityForArticle(@Query("token") String token, @Query("url") String url, Callback<ReadabilityArticle> callback);
}
