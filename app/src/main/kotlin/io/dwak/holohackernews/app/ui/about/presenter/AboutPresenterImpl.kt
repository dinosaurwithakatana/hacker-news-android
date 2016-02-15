package io.dwak.holohackernews.app.ui.about.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.License
import io.dwak.holohackernews.app.ui.about.view.AboutView

class AboutPresenterImpl(view : AboutView, interactorComponent : InteractorComponent)
: AbstractPresenter<AboutView>(view, interactorComponent), AboutPresenter {
  override fun inject() = interactorComponent.inject(this)

  override fun onAttachToView() {
    super.onAttachToView()
    view.displayAbout()

    view.displayLicenses(listOf(License("OkHttp", "License")))
  }
}