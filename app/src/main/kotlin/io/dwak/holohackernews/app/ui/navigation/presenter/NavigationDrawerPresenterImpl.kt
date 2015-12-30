package io.dwak.holohackernews.app.ui.navigation.presenter

import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItem
import io.dwak.holohackernews.app.model.navigation.DrawerItemType
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView
import rx.Observable

class NavigationDrawerPresenterImpl(view : NavigationDrawerView,
                                    interactorComponent: InteractorComponent)
: AbstractPresenter<NavigationDrawerView>(view, interactorComponent), NavigationDrawerPresenter{
    override val drawerItems: Observable<DrawerItem>

    override fun inject() {
        interactorComponent.inject(this)
    }

    init {
        drawerItems = Observable.defer<DrawerItem> {
            Observable.from(Item.values())
                    .map { it.drawerItem }
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
            Item.TOP.drawerItem.id -> view.navigateToStoryList(Feed.TOP)
            Item.BEST.drawerItem.id -> view.navigateToStoryList(Feed.BEST)
            Item.NEW.drawerItem.id -> view.navigateToStoryList(Feed.NEW)
            Item.SHOW.drawerItem.id -> view.navigateToStoryList(Feed.SHOW)
            Item.SHOW_NEW.drawerItem.id -> view.navigateToStoryList(Feed.NEW_SHOW)
            Item.ASK.drawerItem.id -> view.navigateToStoryList(Feed.ASK)
        }
    }

}

enum class Item(val drawerItem : DrawerItem){
    TOP(DrawerItem(0, R.string.title_section_top, R.drawable.ic_navigation_top, DrawerItemType.PRIMARY)),
    BEST(DrawerItem(1, R.string.title_section_best, R.drawable.ic_navigation_best, DrawerItemType.PRIMARY)),
    NEW(DrawerItem(2, R.string.title_section_newest, R.drawable.ic_navigation_new, DrawerItemType.PRIMARY)),
    SHOW(DrawerItem(3, R.string.title_section_show, R.drawable.ic_navigation_show, DrawerItemType.PRIMARY)),
    SHOW_NEW(DrawerItem(4, R.string.title_section_show_new, R.drawable.ic_navigation_new, DrawerItemType.PRIMARY)),
    ASK(DrawerItem(5, R.string.title_section_ask, R.drawable.ic_navigation_ask, DrawerItemType.PRIMARY)),
    DIVIDER0(DrawerItem(type = DrawerItemType.DIVIDER)),
    SETTINGS(DrawerItem(6, titleRes = R.string.title_section_settings, type = DrawerItemType.SECONDARY)),
    ABOUT(DrawerItem(7, titleRes = R.string.title_section_about, type = DrawerItemType.SECONDARY)),
}

