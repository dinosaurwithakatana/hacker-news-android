package io.dwak.holohackernews.app.ui.login;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.util.List;

import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.network.UserService;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Observable;

public class LoginViewModel extends BaseViewModel {
    private final UserService mUserService;
    private String mUserCookie;

    public LoginViewModel() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(true);
        okHttpClient.setFollowSslRedirects(true);
        okHttpClient.networkInterceptors().add(new StethoInterceptor());
        okHttpClient.networkInterceptors().add(chain -> {
            Response response = chain.proceed(chain.request());
            List<String> cookieHeaders = response.headers("set-cookie");
            for (String header : cookieHeaders) {
                if (header.contains("user")) {
                    mUserCookie = header.substring(5);
                    mUserCookie = mUserCookie.split(";")[0];
                }
            }
            return response;
        });

        mUserService = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter())
                                                                 .create()))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(UserService.class);
    }

    public Observable<User> login(String username, String password) {
        return mUserService
                .login("news", username, password)
                .map(response -> new User(username, mUserCookie, true))
                .doOnNext(user -> LocalDataManager.getInstance().saveUser(user));
    }
}
