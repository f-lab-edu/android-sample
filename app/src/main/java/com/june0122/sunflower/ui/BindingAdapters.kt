package com.june0122.sunflower.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.june0122.sunflower.R

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("profileUrl")
    fun loadProfileImage(view: ImageView, url: String) {
        view.load(url) {
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }
    }
}