package io.dwak.holohackernews.app.base.mvp

import android.support.v7.widget.RecyclerView
import android.view.View
import javax.inject.Inject

abstract class MvpViewHolder<T : Presenter>(view: View)
: RecyclerView.ViewHolder(view), DaggerPresenterView {
    protected lateinit var presenter : T @Inject set

    abstract override fun inject()

    init {
        inject()
        presenter.onAttachToView()
    }
}