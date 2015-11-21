package io.dwak.holohackernews.app.dagger.module;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import io.dwak.holohackernews.app.network.HackerNewsService2;
import io.dwak.holohackernews.app.network.TestHackerNewsService2;
import retrofit.Converter;

@Singleton
@Module
public class TestNetworkModule extends NetworkModule {

    @Override
    public HackerNewsService2 getHackerNewsService(@NotNull final String baseUrl, @NotNull final Converter.Factory converterFactory) {
        return new TestHackerNewsService2();
    }
}
