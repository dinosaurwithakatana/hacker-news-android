package io.dwak.holohackernews.app.model.navigation

import io.dwak.holohackernews.app.R

enum class DrawerItem(val drawerItemModel: DrawerItemModel){
    TOP(DrawerItemModel(0, R.string.title_section_top, R.drawable.ic_navigation_top, DrawerItemType.PRIMARY)),
    BEST(DrawerItemModel(1, R.string.title_section_best, R.drawable.ic_navigation_best, DrawerItemType.PRIMARY)),
    NEW(DrawerItemModel(2, R.string.title_section_newest, R.drawable.ic_navigation_new, DrawerItemType.PRIMARY)),
    SHOW(DrawerItemModel(3, R.string.title_section_show, R.drawable.ic_navigation_show, DrawerItemType.PRIMARY)),
    SHOW_NEW(DrawerItemModel(4, R.string.title_section_show_new, R.drawable.ic_navigation_new, DrawerItemType.PRIMARY)),
    ASK(DrawerItemModel(5, R.string.title_section_ask, R.drawable.ic_navigation_ask, DrawerItemType.PRIMARY)),
    DIVIDER0(DrawerItemModel(type = DrawerItemType.DIVIDER)),
    SETTINGS(DrawerItemModel(6, titleRes = R.string.title_section_settings, type = DrawerItemType.SECONDARY)),
    ABOUT(DrawerItemModel(7, titleRes = R.string.title_section_about, type = DrawerItemType.SECONDARY)),
}


