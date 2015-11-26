package io.dwak.holohackernews.app.base.base.mvp.databinding

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dwak.holohackernews.app.base.base.mvp.MvpViewHolder
import io.dwak.holohackernews.app.base.base.mvp.Presenter

abstract class DataBindingMvpViewHolder<P : Presenter, V : ViewDataBinding>(view : View)
: MvpViewHolder<P>(view) {
    var viewBinding : V? = null

    constructor(viewBinding : V) : this(viewBinding.root) {
        this.viewBinding = viewBinding
    }

    companion object {
        fun createViewBinding(layoutInflater: LayoutInflater, @LayoutRes layoutResId: Int, parent: ViewGroup): ViewDataBinding {
            return DataBindingUtil.inflate(layoutInflater, layoutResId, parent, false)
        }
    }
}

