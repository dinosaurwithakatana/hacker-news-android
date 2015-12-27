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
    override fun inject() {
        interactorComponent.inject(this)
    }

    override fun onAttachToView() {
        super.onAttachToView()
        var id = 0
        Observable.create<DrawerItem> {
            if(!it.isUnsubscribed) {
                val s = it
                Feed.values().forEach {
                    s.onNext(DrawerItem(id = id++,
                            titleRes = it.titleRes,
                            iconRes = it.iconRes,
                            type = DrawerItemType.PRIMARY))
                }

                s.onNext(DrawerItem(id++, type = DrawerItemType.DIVIDER))
                s.onNext(DrawerItem(id = id++,
                        titleRes = R.string.title_section_settings,
                        type = DrawerItemType.SECONDARY))
                s.onCompleted()
            }
        }
        .subscribe(view.drawerObserver)
    }

    override fun onDrawerItemClicked() {
        throw UnsupportedOperationException()
    }

}