package io.dwak.holohackernews.app.base.base.mvp

import io.dwak.holohackernews.app.dagger.component.ServiceComponent
import io.dwak.holohackernews.app.network.HackerNewsService2
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

public abstract class AbstractPresenter<T : PresenterView>(val view : T, override val serviceComponent: ServiceComponent)
: DaggerPresenter {
    val viewSubscription = CompositeSubscription()
    lateinit var hackerNewsService : HackerNewsService2 @Inject set

    abstract override fun inject()

    init {
        inject()
    }
}