package com.june0122.sunflower.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.june0122.sunflower.data.repository.UserRepository
import com.june0122.sunflower.ui.main.UserSharedViewModel

class UserListViewModelFactory(
    private val userListAdapter: UserListAdapter,
    private val repository: UserRepository
    ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserSharedViewModel::class.java)) {
            UserSharedViewModel(userListAdapter, repository) as T
        } else {
            throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}