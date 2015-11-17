package io.dwak.holohackernews.app.base.base.mvp

import android.support.v7.widget.RecyclerView
import android.view.View
import javax.inject.Inject

abstract class MvpViewHolder<T : Presenter>(val itemView : View)
: RecyclerView.ViewHolder(itemView), DaggerPresenterView {
    protected lateinit var presenter : T
        @Inject set

    abstract override fun inject()

    init {
        inject()
        presenter.onAttachToView()
    }
}