package com.june0122.sunflower.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.june0122.sunflower.model.data.Progress
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.model.data.UserData
import com.june0122.sunflower.model.data.Users
import com.june0122.sunflower.network.RetrofitClientInstance
import com.june0122.sunflower.ui.adapter.UserListAdapter
import com.june0122.sunflower.utils.Event
import com.june0122.sunflower.utils.UserClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSharedViewModel(
    private val userListAdapter: UserListAdapter,
) : ViewModel(), UserClickListener {
    private val _items = MutableLiveData<List<UserData>>()
    val items: LiveData<List<UserData>> = _items

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _showDetail = MutableLiveData<Event<User>>()
    val showDetail: LiveData<Event<User>> = _showDetail

    private val bookmarkList = mutableListOf<User>()
    private val _bookmarks = MutableLiveData<Event<List<UserData>>>()
    val bookmarks: LiveData<Event<List<UserData>>> = _bookmarks

    private val _bookmarkPosition = MutableLiveData<Int>()
    val bookmarkPosition: LiveData<Int> = _bookmarkPosition

    private val _bookmarkStatus = MutableLiveData<Boolean>()
    val bookmarkStatus: LiveData<Boolean> = _bookmarkStatus

    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0
    private var isLoading = false

    override fun onUserClick(position: Int) {
        val item = userListAdapter[position]
        _showDetail.value = Event(item as User)
    }

    override fun onUserLongClick(position: Int) {
        val item = userListAdapter[position]
    }

    override fun onBookmarkClick(position: Int) {
        val item = userListAdapter[position] as User

        setBookmark(item)
        _bookmarkPosition.value = position
    }

    fun loadNextPage(
        canScrollVertically: Boolean,
        lastVisibleItemPosition: Int,
        smoothScroller: LinearSmoothScroller,
        layoutManager: GridLayoutManager
    ) {
        if (canScrollVertically && lastVisibleItemPosition == userListAdapter.itemCount - 1 && isLoading.not()) {
            if (currentPage < lastPage) {
                addProgress()
                scrollToProgress(smoothScroller, layoutManager)
            }
            currentPage++
            if (currentPage <= lastPage && isLoading.not()) getUserList()
        }
    }

    fun getUserList() {
        isLoading = true

        val userListCall: Call<Users> =
            RetrofitClientInstance().githubService.getUserList(query = "june", perPage, currentPage)

        userListCall.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    response.body()?.let { users -> updateUserList(users) }
                } else if (response.code() == 403) {
                    _statusMessage.value = Event("API rate limit exceeded")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                _statusMessage.value = Event(t.localizedMessage)
            }
        })
    }

    private fun updateUserList(users: Users) {
        if (userListAdapter.itemCount != 0) deleteProgress(progressPosition)
        lastPage = (users.total_count / perPage) + 1

        val newData = users.items.map {
            User(imageUrl = it.avatarUrl, name = it.login, description = "")
        }

        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            addAll(newData)
        }

        isLoading = false
    }

    private fun scrollToProgress(smoothScroller: LinearSmoothScroller, layoutManager: GridLayoutManager) {
        smoothScroller.targetPosition = userListAdapter.itemCount
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun addProgress() {
        progressPosition = userListAdapter.itemCount
        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            add(Progress)
        }
    }

    private fun deleteProgress(position: Int) {
        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            removeAt(position)
        }
    }

    fun setBookmark(data: User) {
        if (data in bookmarkList) {
            _bookmarks.value = Event(bookmarkList.apply { remove(data) })
            _bookmarkStatus.value = false
        } else {
            _bookmarks.value = Event(bookmarkList.apply { add(data) })
            _bookmarkStatus.value = true
        }
    }

    fun checkBookmark(data: User) {
        _bookmarkStatus.value = data in bookmarkList
    }

    fun checkBookmark(position: Int, data: User) {
        _bookmarkStatus.value = data in bookmarkList
        _bookmarkPosition.value = position
    }
}