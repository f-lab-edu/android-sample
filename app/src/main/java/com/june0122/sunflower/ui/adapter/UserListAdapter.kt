package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
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

class UserListAdapter(private val listener: UserClickListener) :
    ListAdapter<UserData, RecyclerView.ViewHolder>(UserDiffCallback()), UserListAdapterEvent {
    private val newList = currentList.toMutableList()

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

    override fun get(position: Int): UserData {
        return currentList[position]
    }

    override fun add(item: UserData) {
        newList.add(item)
//        submitList(newList) // submitList 사용 시 제대로 동작하지 않음
        this.notifyItemInserted(this.itemCount)
    }

    override fun addAll(items: List<UserData>) {
        newList.addAll(items)
        submitList(newList)
    }

    override fun remove(position: Int) {
        newList.removeAt(position)
//        submitList(newList) // submitList 사용 시 제대로 동작하지 않음
        this.notifyItemRemoved(position)
    }

    override fun clear() {
        currentList.clear()
    }

    override fun isEmpty(): Boolean {
        return currentList.isEmpty()
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}