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
import io.dwak.holohackernews.app.dagger.component.DaggerUserServiceComponent;
import io.dwak.holohackernews.app.dagger.module.OkClientModule;
import io.dwak.holohackernews.app.dagger.module.UserServiceModule;
import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.network.UserService;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import rx.Observable;

public class LoginViewModel extends BaseViewModel {
    @Inject UserService mUserService;
    String mUserCookie;

    public LoginViewModel() {

        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new StethoInterceptor());
        interceptors.add(chain -> {
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
        DaggerUserServiceComponent.builder()
                                  .okClientModule(new OkClientModule(interceptors))
                                  .appModule(HackerNewsApplication.getAppModule())
                                  .appComponent(HackerNewsApplication.getAppComponent())
                                  .userServiceModule(new UserServiceModule())
                                  .build()
                                  .inject(this);
    }

    public Observable<User> login(String username, String password) {
        return mUserService
                .login("news", username, password)
                .map(response -> {
                    if(TextUtils.isEmpty(mUserCookie)){
                        throw new IllegalStateException("No User Cookie Found!");
                    }
                    return new User(username, mUserCookie, true);
                })
                .doOnNext(SugarRecord::save)
                .doOnNext(user -> LocalDataManager.getInstance().saveUser(user));
    }
}
