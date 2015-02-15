package io.dwak.holohackernews.app.network;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by vishnu on 2/9/15.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/login")
    Observable<Response> login(@Field("acct") String username, @Field("pw") String password);
}
