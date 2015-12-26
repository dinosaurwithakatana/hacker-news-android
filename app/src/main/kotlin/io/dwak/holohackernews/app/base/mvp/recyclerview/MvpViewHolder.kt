package io.dwak.holohackernews.app.base.mvp.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.base.mvp.dagger.DaggerPresenterView
import javax.inject.Inject

abstract class MvpViewHolder<T : Presenter>(view: View)
: RecyclerView.ViewHolder(view), DaggerPresenterView {
    protected lateinit var presenter : T @Inject set

    abstract override fun inject()

    init {
        inject()
        presenter.prepareToAttachToView()
    }

    fun onRecycle() {
        presenter.onDetachFromView()
    }

}