package io.dwak.holohackernews.app.base.mvp

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding.view.clicks
import rx.Observable
import javax.inject.Inject

abstract class MvpViewHolder<T : Presenter, V>(view: View)
: RecyclerView.ViewHolder(view), DaggerPresenterView {
    protected lateinit var presenter : T @Inject set

    abstract override fun inject()

    init {
        inject()
        presenter.prepareToAttachToView()
    }


    open fun bind(model : V, adapterObservable : Observable<V>) {
        presenter.onAttachToView()
        adapterObservable.mergeWith(itemView.clicks().map { model })
    }

}