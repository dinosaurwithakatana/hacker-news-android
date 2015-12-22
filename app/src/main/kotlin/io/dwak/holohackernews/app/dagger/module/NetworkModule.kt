package io.dwak.holohackernews.app.dagger.module

import com.facebook.stetho.okhttp.StethoInterceptor
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.network.HackerNewsService2
import retrofit.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
public open class NetworkModule {
    open val baseUrl = "https://whispering-fortress-7282.herokuapp.com/"
    open val converterFactory = MoshiConverterFactory.create()

    @Provides @Named(value = "baseUrl") fun providesBaseUrl() = baseUrl

    @Provides fun providesConverterFactory(): Converter.Factory = converterFactory

    @Provides fun providesCallAdapter() : CallAdapter.Factory = RxJavaCallAdapterFactory.create()

    @Provides fun providesNetworkInterceptors(): MutableList<Interceptor> {
        return arrayListOf(StethoInterceptor())
    }

    @Provides fun providesOkClient(interceptors : MutableList<Interceptor>) : OkHttpClient {
        val client  = OkHttpClient()
        client.networkInterceptors().addAll(interceptors)
        return client
    }

    @Singleton @Provides open fun providesHackerNewsService(@Named(value = "baseUrl") baseUrl: String,
                                                            callAdapterFactory: CallAdapter.Factory,
                                                            converterFactory: Converter.Factory,
                                                            okHttpClient : OkHttpClient): HackerNewsService2 {
        return Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(HackerNewsService2::class.java)
    }
}