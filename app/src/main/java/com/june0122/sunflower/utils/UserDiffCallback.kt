package com.june0122.sunflower.utils

import androidx.recyclerview.widget.DiffUtil
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.entity.UserData

class UserDiffCallback(
    private val oldItems: List<UserData>,
    private val newItems: List<UserData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return if (oldItem is User && newItem is User) oldItem.name == newItem.name
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem == newItem
    }
}

//class UserDiffCallback : DiffUtil.ItemCallback<UserData>() {
//    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
//        return if (oldItem is User && newItem is User) oldItem.name == newItem.name
//        else oldItem == newItem
//    }
//
//    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
//        return oldItem == newItem
//    }
//}