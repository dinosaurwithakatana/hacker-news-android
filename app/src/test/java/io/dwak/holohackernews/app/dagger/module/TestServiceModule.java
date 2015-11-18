package io.dwak.holohackernews.app.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import io.dwak.holohackernews.app.network.HackerNewsService2;

@Singleton
@Module
public class TestServiceModule extends ServiceModule{
    @Override
    public HackerNewsService2 getHackerNewsService() {
        return super.getHackerNewsService();
    }
}
