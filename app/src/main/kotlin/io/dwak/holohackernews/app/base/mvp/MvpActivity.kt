package io.dwak.holohackernews.app.base.mvp

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

public abstract class MvpActivity<P : Presenter> : AppCompatActivity(), DaggerPresenterView {
    protected lateinit var presenter : P @Inject set

    abstract override fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
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