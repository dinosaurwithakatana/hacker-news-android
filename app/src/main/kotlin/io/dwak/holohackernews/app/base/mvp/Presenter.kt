package io.dwak.holohackernews.app.base.mvp

import rx.subscriptions.CompositeSubscription

public interface Presenter {
    val viewSubscription : CompositeSubscription

    public open fun prepareToAttachToView() {

    }

    public open fun onAttachToView() {
    }

    public open fun onDetachFromView() {
        viewSubscription.clear()
    }
}