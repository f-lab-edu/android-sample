package com.june0122.sunflower.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.june0122.sunflower.ui.adapter.UserListAdapter

class UserListViewModelFactory(private val userListAdapter: UserListAdapter) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            SharedViewModel(userListAdapter) as T
        } else {
            throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}