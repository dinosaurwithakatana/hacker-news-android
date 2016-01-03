package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import dagger.Provides
import io.dwak.holohackernews.app.HackerNewsApplication
import timber.log.Timber

@Module
class AppModule(private val app : HackerNewsApplication) {
    @Provides fun providesApplicationContext() : HackerNewsApplication = app

    @Provides fun providesTimberTree() : Timber.Tree = Timber.DebugTree()
}