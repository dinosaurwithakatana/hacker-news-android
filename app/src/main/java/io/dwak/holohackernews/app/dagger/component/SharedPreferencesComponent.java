package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.SharedPreferencesModule;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

@Component(dependencies = AppComponent.class,
           modules = SharedPreferencesModule.class)
@Singleton
public interface SharedPreferencesComponent {
    void inject(UserPreferenceManager userPreferenceManager);
    void inject(LocalDataManager localDataManager);
}
