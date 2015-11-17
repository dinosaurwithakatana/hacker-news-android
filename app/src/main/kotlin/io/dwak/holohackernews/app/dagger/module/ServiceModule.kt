package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.network.HackerNewsService2
import retrofit.MoshiConverterFactory
import retrofit.Retrofit
import javax.inject.Singleton

@Singleton
@Module
public open class ServiceModule {
    @Singleton @Provides fun providesHackerNewsService() = getHackerNewsService()

    open fun getHackerNewsService() = Retrofit.Builder()
            .baseUrl("https://whispering-fortress-7282.herokuapp.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(HackerNewsService2::class.java)
}