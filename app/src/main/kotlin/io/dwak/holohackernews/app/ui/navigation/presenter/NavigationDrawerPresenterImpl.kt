package io.dwak.holohackernews.app.ui.navigation.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView

class NavigationDrawerPresenterImpl(view : NavigationDrawerView,
                                    interactorComponent: InteractorComponent)
: AbstractPresenter<NavigationDrawerView>(view, interactorComponent), NavigationDrawerPresenter{
    override fun inject() {
        interactorComponent.inject(this)
    }

    override fun onAttachToView() {
        super.onAttachToView()
        view.populateDrawer(listOf())
    }
}