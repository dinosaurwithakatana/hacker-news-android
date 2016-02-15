package io.dwak.holohackernews.app.ui.navigation.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItem.*
import io.dwak.holohackernews.app.model.navigation.DrawerItemModel
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView
import rx.Observable

class NavigationDrawerPresenterImpl(view : NavigationDrawerView, interactorComponent : InteractorComponent)
: AbstractPresenter<NavigationDrawerView>(view, interactorComponent), NavigationDrawerPresenter {
  override val items : Observable<DrawerItemModel>

  override fun inject() = interactorComponent.inject(this)

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

  override val onItemClick : (Int) -> Unit = {
    id : Int ->
    with(view) {
      when (id) {
        TOP.drawerItemModel.id      -> navigateToStoryList(Feed.TOP)
        BEST.drawerItemModel.id     -> navigateToStoryList(Feed.BEST)
        NEW.drawerItemModel.id      -> navigateToStoryList(Feed.NEW)
        SHOW.drawerItemModel.id     -> navigateToStoryList(Feed.SHOW)
        SHOW_NEW.drawerItemModel.id -> navigateToStoryList(Feed.NEW_SHOW)
        ASK.drawerItemModel.id      -> navigateToStoryList(Feed.ASK)
        SETTINGS.drawerItemModel.id -> navigateToSettings()
        ABOUT.drawerItemModel.id    -> navigateToAbout()
      }
    }
  }

}

