package io.dwak.holohackernews.app.base.base.mvp

import io.dwak.holohackernews.app.dagger.component.ServiceComponent

public interface DaggerPresenter {
    val serviceComponent : ServiceComponent
    fun inject()
}