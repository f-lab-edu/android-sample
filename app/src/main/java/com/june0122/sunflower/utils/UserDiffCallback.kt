package com.june0122.sunflower.utils

import androidx.recyclerview.widget.DiffUtil
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.model.data.UserData

class UserDiffCallback : DiffUtil.ItemCallback<UserData>() {
    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return if (oldItem is User && newItem is User) oldItem.name == newItem.name
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem == newItem
    }
}