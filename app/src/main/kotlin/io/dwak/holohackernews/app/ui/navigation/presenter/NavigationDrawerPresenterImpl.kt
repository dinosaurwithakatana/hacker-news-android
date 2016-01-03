package io.dwak.holohackernews.app.ui.navigation.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItem.*
import io.dwak.holohackernews.app.model.navigation.DrawerItemModel
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView
import rx.Observable

class NavigationDrawerPresenterImpl(view : NavigationDrawerView,
                                    interactorComponent: InteractorComponent)
: AbstractPresenter<NavigationDrawerView>(view, interactorComponent), NavigationDrawerPresenter{
    override val items: Observable<DrawerItemModel>

    override fun inject() {
        interactorComponent.inject(this)
    }

    init {
        items = Observable.defer<DrawerItemModel> {
            Observable.from(values())
                    .map { it.drawerItemModel }
        }
    }

    override fun onAttachToView() {
        super.onAttachToView()

        with(viewSubscription) {
            add(view.drawerClicks
                    ?.subscribe(onItemClick))
        }

        view.navigateToStoryList(Feed.TOP)
    }

    override val onItemClick: (Int) -> Unit = {
        when (it) {
            TOP.drawerItemModel.id -> view.navigateToStoryList(Feed.TOP)
            BEST.drawerItemModel.id -> view.navigateToStoryList(Feed.BEST)
            NEW.drawerItemModel.id -> view.navigateToStoryList(Feed.NEW)
            SHOW.drawerItemModel.id -> view.navigateToStoryList(Feed.SHOW)
            SHOW_NEW.drawerItemModel.id -> view.navigateToStoryList(Feed.NEW_SHOW)
            ASK.drawerItemModel.id -> view.navigateToStoryList(Feed.ASK)
            SETTINGS.drawerItemModel.id -> view.navigateToSettings()
            ABOUT.drawerItemModel.id -> view.navigateToAbout()
        }
    }

}

