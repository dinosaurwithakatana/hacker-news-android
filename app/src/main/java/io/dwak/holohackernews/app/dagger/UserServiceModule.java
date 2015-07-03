package io.dwak.holohackernews.app.dagger;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.UserService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class UserServiceModule {
    private OkHttpClient mOkHttpClient;

    public UserServiceModule(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    @Provides
    UserService providesUserService() {
        return new RestAdapter.Builder()
                .setClient(new OkClient(mOkHttpClient))
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter())
                                                                 .create()))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(UserService.class);
    }
}
