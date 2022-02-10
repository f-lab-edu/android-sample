package com.june0122.sunflower.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun Int.px(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.sp() = this * Resources.getSystem().displayMetrics.scaledDensity

fun convertPixelsToDp(context: Context, px: Int): Int {
    return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun convertDpToPixel(context: Context, dp: Int): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}