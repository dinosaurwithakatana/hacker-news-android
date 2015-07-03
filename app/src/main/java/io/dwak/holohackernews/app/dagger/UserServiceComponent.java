package io.dwak.holohackernews.app.dagger;

import dagger.Component;
import io.dwak.holohackernews.app.ui.login.LoginViewModel;

@Component(modules = UserServiceModule.class)
public interface UserServiceComponent {
    void inject(LoginViewModel viewModel);
}
