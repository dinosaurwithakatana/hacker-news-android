package io.dwak.holohackernews.app.ui.navigation.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter
import io.dwak.holohackernews.app.model.navigation.DrawerItem
import rx.Observable

interface NavigationDrawerPresenter : Presenter {
    val onItemClick : (Int) -> Unit
    val drawerItems : Observable<DrawerItem>
}