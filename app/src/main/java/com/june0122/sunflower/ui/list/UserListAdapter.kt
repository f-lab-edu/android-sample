package com.june0122.sunflower.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.data.entity.Progress
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.entity.UserData
import com.june0122.sunflower.databinding.ItemProgressBinding
import com.june0122.sunflower.databinding.ItemUserListBinding
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.UserDiffCallback

class UserListAdapter : ListAdapter<UserData, RecyclerView.ViewHolder>(UserDiffCallback()) {
    lateinit var listener: UserClickListener

    operator fun get(position: Int): UserData = currentList[position]

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is Progress -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding =
                    ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserListViewHolder(binding, listener)
            }
            else -> {
                val binding =
                    ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProgressHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = currentList[holder.absoluteAdapterPosition]) {
            is User -> if (holder is UserListViewHolder) holder.bind(data)
            is Progress -> {}
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}