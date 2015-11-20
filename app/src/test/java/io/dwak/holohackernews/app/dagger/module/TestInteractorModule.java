package io.dwak.holohackernews.app.dagger.module;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public class TestInteractorModule extends InteractorModule {
    public TestInteractorModule() {
        System.out.println("TestInteractor");
    }

    @NotNull
    @Override
    public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
    }
}
