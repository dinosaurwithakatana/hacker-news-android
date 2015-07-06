package io.dwak.holohackernews.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import io.dwak.holohackernews.app.dagger.module.UserServiceModule;
import io.dwak.holohackernews.app.ui.login.LoginViewModel;

@Singleton
@Component(dependencies = {AppComponent.class},
           modules = UserServiceModule.class)
public interface UserServiceComponent {
    void inject(LoginViewModel viewModel);
}
