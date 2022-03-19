package com.june0122.sunflower.ui.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.june0122.sunflower.R
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.databinding.ItemUserListBinding
import com.june0122.sunflower.utils.UserClickListener

class UserListViewHolder(binding: ItemUserListBinding, listener: UserClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    private val plantNameTextView = binding.tvPlantName
    private val availablePlantImageView = binding.imgAvailablePlant
    private val bookmarkButton = binding.btnBookmark

    init {
        itemView.setOnClickListener { listener.onUserClick(absoluteAdapterPosition) }
        itemView.setOnLongClickListener {
            listener.onUserLongClick(absoluteAdapterPosition)
            return@setOnLongClickListener true
        }
        bookmarkButton.setOnClickListener {
            listener.onBookmarkClick(absoluteAdapterPosition)
            if (bookmarkButton.isSelected) {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark)
                bookmarkButton.isSelected = false
            }
            else {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled)
                bookmarkButton.isSelected = true
            }
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

//        if (user.isBookmark) {
//            bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled)
//            bookmarkButton.isSelected = true
//        } else {
//            bookmarkButton.setImageResource(R.drawable.ic_bookmark)
//            bookmarkButton.isSelected = false
//        }
    }
}