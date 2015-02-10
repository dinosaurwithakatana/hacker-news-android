package io.dwak.holohackernews.app.network;

import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

/**
 * Created by vishnu on 2/9/15.
 */
public interface LoginService {
    @GET("/")
    Observable getFrontPage(@Header("user") String header);
}
