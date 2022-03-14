package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.databinding.ItemProgressBinding
import com.june0122.sunflower.databinding.ItemUserListBinding
import com.june0122.sunflower.model.data.Progress
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.model.data.UserData
import com.june0122.sunflower.ui.viewholder.ProgressHolder
import com.june0122.sunflower.ui.viewholder.UserListViewHolder
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.UserDiffCallback

class UserListAdapter(private val listener: UserClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val userList = mutableListOf<UserData>()

    operator fun get(position: Int): UserData = userList[position]

    override fun getItemViewType(position: Int): Int {
        return when (userList[position]) {
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

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = userList[holder.absoluteAdapterPosition]) {
            is User -> if (holder is UserListViewHolder) holder.bind(data)
            is Progress -> {}
        }
    }

    fun updateUserListItems(items: List<UserData>) {
        val diffCallback = UserDiffCallback(userList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userList.clear()
        userList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}