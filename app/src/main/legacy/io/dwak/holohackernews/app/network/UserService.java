package io.dwak.holohackernews.app.network;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public interface UserService {
    @FormUrlEncoded
    @POST("/comment")
    Observable<Object> postComment(@Header("Cookie") String cookie,
                                   @Field("goto") String gotoParam,
                                   @Field("hmac") String hmac,
                                   @Field("parent") Long parentId,
                                   @Field("text") String text);

    @GET("/vote")
    Observable<Object> vote(@Header("Cookie") String cookie,
                            @Query("for") Long parentId,
                            @Query("dir") String direction,
                            @Query("auth") String auth,
                            @Query("goto") String gotoParam);
}
