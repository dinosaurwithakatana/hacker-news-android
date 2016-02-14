package io.dwak.holohackernews.app.base.mvp

import rx.subscriptions.CompositeSubscription

interface Presenter {
    val viewSubscription : CompositeSubscription

    open fun prepareToAttachToView() {

    }

    open fun onAttachToView() {
    }

    open fun onDetachFromView() {
        viewSubscription.clear()
    }
}