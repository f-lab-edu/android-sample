package com.june0122.sunflower.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val imageUrl: String,
    val name: String,
    val description: String,
    ) : Parcelable