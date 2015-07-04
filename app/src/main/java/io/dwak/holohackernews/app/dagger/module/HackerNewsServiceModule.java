package io.dwak.holohackernews.app.dagger.module;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(includes = AppModule.class)
public class HackerNewsServiceModule {
    @Provides
    @Singleton
    HackerNewsService provideService(@Named("gson") Gson gson){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                .build();

        return restAdapter.create(HackerNewsService.class);
    }
}
