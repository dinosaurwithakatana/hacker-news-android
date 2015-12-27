package io.dwak.holohackernews.app.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import io.dwak.holohackernews.app.R

enum class Feed(@StringRes val titleRes: Int,
                @DrawableRes val iconRes : Int) {
    TOP(R.string.title_section_top,
            R.drawable.ic_navigation_top),
    NEW(R.string.title_section_newest,
            R.drawable.ic_navigation_new),
    BEST(R.string.title_section_best,
            R.drawable.ic_navigation_best),
    SHOW(R.string.title_section_show,
            R.drawable.ic_navigation_show),
    NEW_SHOW(R.string.title_section_show_new,
            R.drawable.ic_navigation_new),
    ASK(R.string.title_section_ask,
            R.drawable.ic_navigation_ask)
}