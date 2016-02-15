package io.dwak.holohackernews.app.dagger.interactor

import io.dwak.holohackernews.app.model.TextSize

interface UserPreferenceInteractor {
  fun shouldShowLinkFirst() : Boolean
  fun preferredTextSize() : TextSize
  fun shouldUseExternalBrowser() : Boolean
  fun shouldUseNightMode() : Boolean
  fun allowSwipeBackFromDetails() : Boolean
}