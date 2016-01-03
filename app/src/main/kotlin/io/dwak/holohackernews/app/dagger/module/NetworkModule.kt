package io.dwak.holohackernews.app.dagger.module

import com.facebook.stetho.okhttp.StethoInterceptor
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.network.HackerNewsService
import retrofit.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
public open class NetworkModule {

    @Provides @Named(value = "baseUrl") fun getBaseUrl(): String = "https://whispering-fortress-7282.herokuapp.com/"

    @Provides fun converterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides fun callAdapter() : CallAdapter.Factory = RxJavaCallAdapterFactory.create()

    @Provides fun networkInterceptors(): MutableList<Interceptor> = arrayListOf(StethoInterceptor())

    @Provides fun okClient(interceptors : MutableList<Interceptor>) : OkHttpClient {
        val client  = OkHttpClient()
        client.networkInterceptors().addAll(interceptors)
        return client
    }

    @Singleton @Provides open fun hackerNewsService(@Named(value = "baseUrl") baseUrl: String,
                                                    callAdapterFactory: CallAdapter.Factory,
                                                    converterFactory: Converter.Factory,
                                                    okHttpClient: OkHttpClient): HackerNewsService
            = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .build()
            .create(HackerNewsService::class.java)
}