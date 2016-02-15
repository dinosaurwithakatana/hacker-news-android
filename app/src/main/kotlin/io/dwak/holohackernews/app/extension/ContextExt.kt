package io.dwak.holohackernews.app.extension

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat


@ColorInt fun Context.getColorCompat(@ColorRes colorResId : Int)
        = ContextCompat.getColor(this, colorResId)
