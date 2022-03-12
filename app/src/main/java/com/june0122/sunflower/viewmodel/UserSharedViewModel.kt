package com.june0122.sunflower.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.R
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
    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _showDetail = MutableLiveData<Event<User>>()
    val showDetail: LiveData<Event<User>> = _showDetail

    private val bookmarkList = mutableListOf<User>()
    private val _bookmarks = MutableLiveData<List<UserData>>()
    val bookmarks: LiveData<List<UserData>> = _bookmarks

    private var itemCount = 0
    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0

    override fun onUserClick(position: Int) {
        val item = userListAdapter[position]
        _showDetail.value = Event(item as User)
    }

    override fun onUserLongClick(position: Int) {
        val item = userListAdapter[position]
    }

    fun loadNextPage(
        canScrollVertically: Boolean,
        lastVisibleItemPosition: Int,
        smoothScroller: LinearSmoothScroller,
        layoutManager: GridLayoutManager
    ) {
        if (canScrollVertically && lastVisibleItemPosition == itemCount - 1) {
            progressPosition = itemCount
            if (currentPage < lastPage) addProgress()
            currentPage++
            if (currentPage <= lastPage) getUserList()
            scrollToProgress(smoothScroller, layoutManager)
        }
    }

    fun getUserList() {
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
        if (itemCount != 0) deleteProgress(progressPosition)
        lastPage = (users.total_count / perPage) + 1
        val newData = users.items.map {
            User(imageUrl = it.avatarUrl, name = it.login, description = "")
        }
        userListAdapter.addAll(newData)
        itemCount += newData.size
    }

    private fun scrollToProgress(smoothScroller: LinearSmoothScroller, layoutManager: GridLayoutManager) {
        smoothScroller.targetPosition = userListAdapter.itemCount
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun addProgress() {
        userListAdapter.add(Progress)
    }

    private fun deleteProgress(position: Int) {
        userListAdapter.remove(position)
    }

    fun setBookmark(fab: FloatingActionButton, data: User) {
        if (data in bookmarkList) {
            _bookmarks.value = bookmarkList.apply { remove(data) }
            fab.setImageResource(R.drawable.ic_bookmark)
            Snackbar.make(fab, "Disable bookmark", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        } else {
            _bookmarks.value = bookmarkList.apply { add(data) }
            fab.setImageResource(R.drawable.ic_bookmark_filled)
            Snackbar.make(fab, "Enable bookmark", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
    }

    fun checkBookmark(fab: FloatingActionButton, data: User) {
        if (data in bookmarkList) {
            fab.setImageResource(R.drawable.ic_bookmark_filled)
        }
    }
}