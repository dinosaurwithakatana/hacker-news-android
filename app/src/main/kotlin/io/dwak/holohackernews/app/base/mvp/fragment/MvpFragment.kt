package io.dwak.holohackernews.app.base.mvp.fragment

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxFragment
import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.base.mvp.dagger.DaggerPresenterView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.component.PresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import javax.inject.Inject

abstract class MvpFragment<P : Presenter> : RxFragment(), DaggerPresenterView {
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

  fun objectGraph(t : PresenterView) : PresenterComponent {
    return DaggerPresenterComponent.builder()
            .presenterModule(PresenterModule(t))
            .interactorComponent(DaggerInteractorComponent.builder()
                                         .interactorModule(InteractorModule(activity))
                                         .build())
            .build()
  }
}

