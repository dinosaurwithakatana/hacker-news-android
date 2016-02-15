package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.Presenter

interface StoryDetailPresenter : Presenter {
  var itemId : Long?
  fun getStoryDetails()
  fun headerScrolled(headerVisible : Boolean)
}