package io.dwak.holohackernews.app.model.navigation

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

enum class DrawerItemType { PRIMARY, SECONDARY, DIVIDER }

data class DrawerItemModel(
        val id : Int = -1,
        @StringRes val titleRes : Int = -1,
        @DrawableRes val iconRes : Int = -1,
        val type : DrawerItemType)

