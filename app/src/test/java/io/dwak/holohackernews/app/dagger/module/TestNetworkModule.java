package io.dwak.holohackernews.app.dagger.module;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import io.dwak.holohackernews.app.network.HackerNewsService2;
import retrofit.Converter;

import static org.mockito.Mockito.mock;

@Singleton
@Module
public class TestNetworkModule extends NetworkModule {
    public final HackerNewsService2 mockedService = mock(HackerNewsService2.class);

    @Override
    public HackerNewsService2 getHackerNewsService(@NotNull final String baseUrl, @NotNull final Converter.Factory converterFactory) {
        return mockedService;
    }
}
