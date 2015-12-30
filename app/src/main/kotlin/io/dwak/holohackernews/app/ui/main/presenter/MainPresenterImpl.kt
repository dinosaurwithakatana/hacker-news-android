package io.dwak.holohackernews.app.ui.main.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.ui.main.view.MainView

class MainPresenterImpl(view : MainView, interactorComponent: InteractorComponent)
: AbstractPresenter<MainView>(view, interactorComponent), MainPresenter {

    override fun inject() = interactorComponent.inject(this)

    override fun onAttachToView() {
        super.onAttachToView()
    }
}