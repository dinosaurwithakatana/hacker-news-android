package io.dwak.holohackernews.app.base.mvp.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.base.mvp.dagger.DaggerPresenterView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.component.PresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import javax.inject.Inject

abstract class MvpViewHolder<T : Presenter>(view : View)
: RecyclerView.ViewHolder(view), DaggerPresenterView {
  protected lateinit var presenter : T @Inject set

  abstract override fun inject()

  init {
    inject()
    presenter.prepareToAttachToView()
  }

  fun onBind() = presenter.onAttachToView()
  fun onRecycle() = presenter.onDetachFromView()

  fun objectGraph(v : PresenterView) : PresenterComponent {
    return DaggerPresenterComponent.builder()
            .interactorComponent(DaggerInteractorComponent.builder()
                                         .interactorModule(InteractorModule(itemView.context))
                                         .build())
            .presenterModule(PresenterModule(v))
            .build()
  }
}