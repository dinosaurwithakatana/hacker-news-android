package io.dwak.holohackernews.app.extension

import android.webkit.WebSettings

var WebSettings.supportZoom : Boolean
  get() = supportZoom()
  set(value) = setSupportZoom(value)

