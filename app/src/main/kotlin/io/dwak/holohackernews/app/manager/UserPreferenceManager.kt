package io.dwak.holohackernews.app.manager

import android.content.SharedPreferences
import io.dwak.holohackernews.app.HackerNewsApplication
import io.dwak.holohackernews.app.dagger.interactor.UserPreferenceInteractor
import io.dwak.holohackernews.app.model.TextSize
import javax.inject.Inject

object UserPreferenceManager : UserPreferenceInteractor {
  lateinit var sharedPrefs : SharedPreferences @Inject set
  val SHOULD_USE_EXTERNAL_BROWSER = "pref_system_browser";
  val PREF_LINK_FIRST = "pref_link_first";
  val PREF_LIST_ANIMATIONS = "pref_list_animations";
  val PREF_NIGHT_MODE = "pref_night_mode";
  val PREF_TEXT_SIZE = "pref_text_size";
  val PREF_SWIPE_BACK = "pref_swipe_back";

  init {
    HackerNewsApplication.instance.appComponent.inject(this)
  }

  override fun allowSwipeBackFromDetails() = sharedPrefs.getBoolean(PREF_SWIPE_BACK, true)

  override fun preferredTextSize() = TextSize.valueOf(sharedPrefs.getString(PREF_TEXT_SIZE,
                                                                            "medium"))

  override fun shouldShowLinkFirst() : Boolean = sharedPrefs.getBoolean(PREF_LINK_FIRST, false)

  override fun shouldUseExternalBrowser() = sharedPrefs.getBoolean(SHOULD_USE_EXTERNAL_BROWSER,
                                                                   false)

  override fun shouldUseNightMode() = sharedPrefs.getBoolean(PREF_NIGHT_MODE, false)
}