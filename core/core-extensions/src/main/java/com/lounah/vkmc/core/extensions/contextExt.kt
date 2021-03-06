package com.lounah.vkmc.core.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.Surface
import android.view.WindowManager


fun Float.dp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun Int.dp(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
