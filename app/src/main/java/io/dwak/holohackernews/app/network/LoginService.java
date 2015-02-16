package io.dwak.holohackernews.app.network;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by vishnu on 2/9/15.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/x")
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Encoding: gzip, deflate",
            "Host: news.ycombinator.com",
            "Origin: https://news.ycombinator.com",
            "User-Agent: HTTPie/0.8.0"
    })
    Observable<Response> login(@Field("goto") String goTo, @Field("acct") String username, @Field("pw") String password);
}
