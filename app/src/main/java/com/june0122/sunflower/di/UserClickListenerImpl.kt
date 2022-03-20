package com.june0122.sunflower.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.june0122.sunflower.ui.main.UserSharedViewModel
import com.june0122.sunflower.utils.UserClickListener
import javax.inject.Inject

class UserClickListenerImpl @Inject constructor(activity: FragmentActivity) : UserClickListener {
    private val viewModel = ViewModelProvider(activity)[UserSharedViewModel::class.java]

    override fun onUserClick(position: Int) {
        viewModel.onUserClick(position)
    }

    override fun onUserLongClick(position: Int) {
        viewModel.onUserLongClick(position)
    }

    override fun onBookmarkClick(position: Int) {
        viewModel.onBookmarkClick(position)
    }
}