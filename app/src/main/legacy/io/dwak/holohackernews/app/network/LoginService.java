package io.dwak.holohackernews.app.network;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

public interface LoginService {
    @FormUrlEncoded
    @POST("/login")
    Observable<Response> login(@Field("go_to") String goTo, @Field("acct") String username, @Field("pw") String password);
}
