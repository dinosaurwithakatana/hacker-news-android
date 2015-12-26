package io.dwak.holohackernews.app.model

import android.support.annotation.StringRes
import io.dwak.holohackernews.app.R

enum class Feed(@StringRes val titleRes: Int) {
    TOP(R.string.title_section_top),
    NEW(R.string.title_section_newest),
    BEST(R.string.title_section_best),
    SHOW(R.string.title_section_show),
    NEW_SHOW(R.string.title_section_show_new),
    ASK(R.string.title_section_ask)
}