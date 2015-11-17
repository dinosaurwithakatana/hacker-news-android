package io.dwak.holohackernews.app.dagger.module

import dagger.Module
import io.dwak.holohackernews.app.base.base.mvp.PresenterView

@Module
class PresenterModule(val view : PresenterView) {
}