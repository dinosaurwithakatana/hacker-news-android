package io.dwak.holohackernews.app.dagger.module;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.network.UserService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(includes = AppModule.class)
public class UserServiceModule {
    private OkHttpClient mOkHttpClient;

    public UserServiceModule(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    @Provides
    @Singleton
    UserService providesUserService(@Named("gson") Gson gson) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(mOkHttpClient))
                .setConverter(new GsonConverter(gson))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(UserService.class);
    }
}
