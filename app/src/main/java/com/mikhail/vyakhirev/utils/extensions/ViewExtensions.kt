package com.mikhail.vyakhirev.utils.extensions

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources

fun View.visible() {
    if (visibility == View.VISIBLE) return
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible(flag: Boolean) = if (flag) visible() else gone()

fun View.colorStateList(@ColorRes color: Int): ColorStateList =
    AppCompatResources.getColorStateList(context, color)