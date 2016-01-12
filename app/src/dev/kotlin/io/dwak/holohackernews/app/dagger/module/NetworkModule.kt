package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.network.HackerNewsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
public open class NetworkModule {

    @Provides @Named(value = "baseUrl") fun getBaseUrl(): String = "https://whispering-fortress-7282.herokuapp.com/"

    @Provides fun converterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides fun callAdapter() : CallAdapter.Factory = RxJavaCallAdapterFactory.create()

    @Provides fun httpLogLevel() : HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC

    @Provides fun httpLogger() = HttpLoggingInterceptor.Logger { Timber.tag("Retrofit").v(it) }

    @Provides fun networkInterceptors(logger : HttpLoggingInterceptor.Logger,
                                      logLevel : HttpLoggingInterceptor.Level) : MutableList<Interceptor> {
        val loggingInterceptor = HttpLoggingInterceptor(logger)
        loggingInterceptor.setLevel(logLevel)
        return arrayListOf(loggingInterceptor)
    }

    @Provides fun okClient(interceptors : MutableList<Interceptor>) : OkHttpClient {
        val clientBuilder = OkHttpClient().newBuilder()
        interceptors.forEach { clientBuilder.addInterceptor(it) }
        return clientBuilder.build()
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