package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.base.mvp.Presenter

interface StoryDetailPresenter : Presenter {
    val itemId : Long
}