package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.HackerNewsApplication

@Module
class AppModule(val app : HackerNewsApplication) {
    @Provides fun providesApplicationContext() : HackerNewsApplication{
        return app
    }
}