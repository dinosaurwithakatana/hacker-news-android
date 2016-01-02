package io.dwak.holohackernews.app.base.mvp.fragment

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxFragment
import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.base.mvp.dagger.DaggerPresenterView
import javax.inject.Inject

public abstract class MvpFragment<P : Presenter> : RxFragment(), DaggerPresenterView {
    protected lateinit var presenter : P
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