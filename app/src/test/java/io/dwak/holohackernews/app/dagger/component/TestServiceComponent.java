package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.TestServiceModule;

@Singleton
@Component(modules = TestServiceModule.class)
public interface TestServiceComponent extends ServiceComponent{
}
