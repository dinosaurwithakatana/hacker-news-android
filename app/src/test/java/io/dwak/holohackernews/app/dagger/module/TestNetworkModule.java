package io.dwak.holohackernews.app.dagger.module;

import com.squareup.okhttp.OkHttpClient;

import org.jetbrains.annotations.NotNull;

import javax.inject.Named;

import io.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.CallAdapter;
import retrofit.Converter;

import static org.mockito.Mockito.mock;

public class TestNetworkModule extends NetworkModule {
    public final HackerNewsService mockedService = mock(HackerNewsService.class);

    @NotNull
    @Override
    public HackerNewsService hackerNewsService(@Named("baseUrl") @NotNull String baseUrl,
                                               @NotNull CallAdapter.Factory callAdapterFactory,
                                               @NotNull Converter.Factory converterFactory,
                                               @NotNull OkHttpClient okHttpClient) {
        return mockedService;
    }
}
