package io.dwak.holohackernews.app.ui.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.LoginService;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel{
    private final LoginService mLoginService;

    public LoginViewModel() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Long.class, new LongTypeAdapter());
        Gson gson = gsonBuilder.create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient().setFollowSslRedirects(true)))
                .setConverter(new GsonConverter(gson))
                .setEndpoint("https://news.ycombinator.com")
                .build();
        mLoginService = restAdapter.create(LoginService.class);
    }

    public Observable<String> login(String username, String password) {
        return mLoginService
                .login("news", username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    String userCookie = null;
                    for (Header header : response.getHeaders()) {
                        if ("set-cookie".equals(header.getName())) {
                            if (header.getValue().contains("user")) {
                                userCookie = header.getValue().substring(5);
                                userCookie = userCookie.split(";")[0];
                            }
                        }
                    }
                    return userCookie;
                });
    }
}
