package io.dwak.holohackernews.app.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import io.dwak.holohackernews.app.R

enum class Feed(val id : Int,
                @StringRes val titleRes: Int,
                @DrawableRes val iconRes : Int) {
    TOP(0, R.string.title_section_top, R.drawable.ic_navigation_top),
    NEW(1, R.string.title_section_newest, R.drawable.ic_navigation_new),
    BEST(2, R.string.title_section_best, R.drawable.ic_navigation_best),
    SHOW(3, R.string.title_section_show, R.drawable.ic_navigation_show),
    NEW_SHOW(4, R.string.title_section_show_new, R.drawable.ic_navigation_new),
    ASK(5, R.string.title_section_ask, R.drawable.ic_navigation_ask)
}