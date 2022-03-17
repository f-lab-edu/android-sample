package com.june0122.sunflower.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserListViewModelFactory(private val userListAdapter: UserListAdapter) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserSharedViewModel::class.java)) {
            UserSharedViewModel(userListAdapter) as T
        } else {
            throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}