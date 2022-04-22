package com.june0122.sunflower.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.databinding.ItemUserListBinding
import com.june0122.sunflower.utils.UserClickListener

class UserListViewHolder(private val binding: ItemUserListBinding, private val listener: UserClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) = with(binding) {
        this.user = user
        this.position = absoluteAdapterPosition
        this.userClickListener = listener
        executePendingBindings()
    }
}