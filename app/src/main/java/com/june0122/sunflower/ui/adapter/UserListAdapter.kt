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
    private val _items = mutableListOf<UserData>()
    val items = _items

    override fun getItemViewType(position: Int): Int {
        return when (_items[position]) {
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

//    override fun getItemCount(): Int = _items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = _items[holder.absoluteAdapterPosition]) {
            is User -> if (holder is UserListViewHolder) holder.bind(data)
            is Progress -> {}
        }
    }

    override fun get(position: Int): UserData {
        return _items[position]
    }

    override fun add(item: UserData) {
        _items.add(item)
        this.notifyItemInserted(_items.count())
    }

    override fun addAll(items: List<UserData>) {
        _items.addAll(items)
        this.notifyItemRangeInserted(itemCount, items.size)
    }

    override fun remove(position: Int) {
        _items.removeAt(position)
        this.notifyItemRemoved(position)
    }

    override fun clear() {
        _items.clear()
    }

    override fun isEmpty(): Boolean {
        return _items.isEmpty()
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}