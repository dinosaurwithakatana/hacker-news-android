package io.dwak.holohackernews.app.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class HackerNewsServiceModule {
    @Provides
    HackerNewsService provideService(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter())
                                     .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                .build();

        return restAdapter.create(HackerNewsService.class);
    }
}
