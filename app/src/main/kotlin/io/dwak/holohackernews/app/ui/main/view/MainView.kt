package io.dwak.holohackernews.app.ui.main.view

import io.dwak.holohackernews.app.base.mvp.PresenterView

interface MainView : PresenterView {
  fun navigateToStoryDetail(itemId : Long)
}