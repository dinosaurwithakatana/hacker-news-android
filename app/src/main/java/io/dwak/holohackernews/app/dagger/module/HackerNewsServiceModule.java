package io.dwak.holohackernews.app.dagger.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(includes = {AppModule.class, OkClientModule.class})
public class HackerNewsServiceModule {
    @Provides
    @Singleton
    HackerNewsService provideService(@Named("gson") Gson gson, @Named("okclient") OkClient okClient){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setClient(okClient)
                .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                .build();

        return restAdapter.create(HackerNewsService.class);
    }
}
