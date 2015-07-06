package io.dwak.holohackernews.app.dagger.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.network.UserService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(includes = {AppModule.class, OkClientModule.class})
public class UserServiceModule {
    @Provides
    @Singleton
    UserService providesUserService(@Named("gson") Gson gson, @Named("okclient") OkClient okClient) {
        return new RestAdapter.Builder()
                .setClient(okClient)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(UserService.class);
    }
}
