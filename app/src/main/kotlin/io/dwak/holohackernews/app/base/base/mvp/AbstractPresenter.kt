package io.dwak.holohackernews.app.base.base.mvp

import io.dwak.holohackernews.app.dagger.component.NetworkComponent
import io.dwak.holohackernews.app.network.HackerNewsService2
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

public abstract class AbstractPresenter<T : PresenterView>(val view : T, val networkComponent: NetworkComponent)
: DaggerPresenter {
    val viewSubscription = CompositeSubscription()

    abstract override fun inject()

    init {
        inject()
    }
}