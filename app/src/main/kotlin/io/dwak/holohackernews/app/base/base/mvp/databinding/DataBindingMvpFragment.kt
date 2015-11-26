package io.dwak.holohackernews.app.base.base.mvp.databinding

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import io.dwak.holohackernews.app.base.base.mvp.MvpFragment
import io.dwak.holohackernews.app.base.base.mvp.Presenter

abstract class DataBindingMvpFragment<P : Presenter, V : ViewDataBinding>
: MvpFragment<P>() {
    lateinit var viewBinding : V

    fun createViewBinding(layoutInflater: LayoutInflater, @LayoutRes layoutResId : Int, container : ViewGroup){
        viewBinding = DataBindingUtil.inflate(layoutInflater,layoutResId, container, false)
    }
}