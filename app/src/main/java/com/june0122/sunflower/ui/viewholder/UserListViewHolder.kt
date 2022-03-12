package com.june0122.sunflower.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.ItemUserListBinding
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.utils.UserClickListener

class UserListViewHolder(binding: ItemUserListBinding, listener: UserClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    private val plantNameTextView = binding.tvPlantName
    private val availablePlantImageView = binding.imgAvailablePlant

    init {
        itemView.setOnClickListener { listener.onUserClick(absoluteAdapterPosition) }
        itemView.setOnLongClickListener {
            listener.onUserLongClick(absoluteAdapterPosition)
            return@setOnLongClickListener true
        }
    }

    fun bind(user: User) {
        availablePlantImageView.load(user.imageUrl) {
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }
        plantNameTextView.text = user.name
    }
}