package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.TestInteractorModule;
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule;

@Singleton
@Component(modules = {TestNetworkModule.class, TestInteractorModule.class})
public interface TestNetworkComponent extends NetworkComponent {
}
