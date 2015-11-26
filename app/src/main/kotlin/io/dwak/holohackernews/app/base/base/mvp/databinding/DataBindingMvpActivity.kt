package io.dwak.holohackernews.app.base.base.mvp.databinding

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import io.dwak.holohackernews.app.base.base.mvp.MvpActivity
import io.dwak.holohackernews.app.base.base.mvp.Presenter
import java.util.zip.Inflater

abstract class DataBindingMvpActivity<P : Presenter, V : ViewDataBinding> : MvpActivity<P>() {
    lateinit var viewBinding : V
    fun createViewBinding(@LayoutRes layoutResId : Int){
        viewBinding = DataBindingUtil.setContentView(this, layoutResId)
    }
}