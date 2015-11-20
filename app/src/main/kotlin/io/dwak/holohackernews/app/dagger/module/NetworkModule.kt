package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.network.HackerNewsService2
import retrofit.Converter
import retrofit.MoshiConverterFactory
import retrofit.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
public open class NetworkModule {
    open val baseUrl = "https://whispering-fortress-7282.herokuapp.com/"
    open val converterFactory = MoshiConverterFactory.create()

    @Provides @Named(value = "baseUrl") fun providesBaseUrl() = baseUrl
    @Provides fun providesConverterFactory(): Converter.Factory = converterFactory
    @Singleton @Provides fun providesHackerNewsService(@Named(value = "baseUrl") baseUrl : String, converterFactory: Converter.Factory) = getHackerNewsService(baseUrl, converterFactory)

    open fun getHackerNewsService(baseUrl : String, converterFactory: Converter.Factory) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
            .create(HackerNewsService2::class.java)

}