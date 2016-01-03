package io.dwak.holohackernews.app.ui.about.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.License

interface AboutView : PresenterView {
    fun displayAbout()
    fun displayLicenses(licenses : List<License>)
}