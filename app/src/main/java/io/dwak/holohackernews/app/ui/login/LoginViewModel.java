package io.dwak.holohackernews.app.ui.login;

import android.text.TextUtils;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.orm.SugarRecord;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkServiceComponent;
import io.dwak.holohackernews.app.dagger.module.OkClientModule;
import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.network.LoginService;
import rx.Observable;

public class LoginViewModel extends BaseViewModel {
    String mCfduid;
    @Inject LoginService mLoginService;
    String mUserCookie;

    public LoginViewModel() {

        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new StethoInterceptor());
        interceptors.add(chain -> {
            Response response = chain.proceed(chain.request());
            List<String> cookieHeaders = response.headers("set-cookie");
            for (String header : cookieHeaders) {
                if (header.contains("user")) {
                    mUserCookie = header.split(";")[0];
                }
                else if(header.contains("__cfduid")){
                    mCfduid = header.split(";")[0];
                }
            }
            return response;
        });
        DaggerNetworkServiceComponent.builder()
                                     .okClientModule(new OkClientModule(interceptors))
                                     .appModule(HackerNewsApplication.getAppModule())
                                     .appComponent(HackerNewsApplication.getAppComponent())
                                     .build()
                                     .inject(this);
    }

    public Observable<User> login(String username, String password) {
        return mLoginService
                .login("news", username, password)
                .map(response -> {
                    if (TextUtils.isEmpty(mUserCookie)) {
                        throw new RuntimeException("No User Cookie Found!");
                    }
                    return new User(username, mCfduid + ";" + mUserCookie, true);
                })
                .doOnNext(SugarRecord::save);
    }
}
