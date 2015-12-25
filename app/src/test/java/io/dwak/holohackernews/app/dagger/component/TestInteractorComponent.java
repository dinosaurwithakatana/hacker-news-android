package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.InteractorModule;
import io.dwak.holohackernews.app.dagger.module.NetworkModule;

@Singleton
@Component(modules = {NetworkModule.class, InteractorModule.class})
public interface TestInteractorComponent extends InteractorComponent {
}
