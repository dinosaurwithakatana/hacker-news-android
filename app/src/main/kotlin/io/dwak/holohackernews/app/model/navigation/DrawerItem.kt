package io.dwak.holohackernews.app.model.navigation

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class DrawerItem(val id : Int,
                      @DrawableRes val iconRes : Int,
                      @StringRes val titleRes : Int,
                      val type : DrawerItemType)

enum class DrawerItemType {
    PRIMARY, SECONDARY
}