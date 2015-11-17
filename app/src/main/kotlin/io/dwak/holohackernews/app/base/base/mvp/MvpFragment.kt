package io.dwak.holohackernews.app.base.base.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import io.dwak.holohackernews.app.base.base.mvp.DaggerPresenterView
import io.dwak.holohackernews.app.base.base.mvp.Presenter
import javax.inject.Inject

public abstract class MvpFragment<T : Presenter> : Fragment(), DaggerPresenterView {
    protected lateinit var presenter : T
        @Inject set

    abstract override fun inject()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        presenter.prepareToAttachToView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onAttachToView()
    }

    override fun onPause() {
        super.onPause()
        presenter.onDetachFromView()
    }
}