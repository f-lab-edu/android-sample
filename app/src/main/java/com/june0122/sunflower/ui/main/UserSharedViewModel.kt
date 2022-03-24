package com.june0122.sunflower.ui.main

import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.june0122.sunflower.data.api.GithubService
import com.june0122.sunflower.data.entity.Progress
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.entity.UserData
import com.june0122.sunflower.data.entity.Users
import com.june0122.sunflower.data.repository.UserRepository
import com.june0122.sunflower.ui.list.UserListAdapter
import com.june0122.sunflower.utils.Event
import com.june0122.sunflower.utils.UserClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserSharedViewModel @Inject constructor(private val repository: UserRepository) : ViewModel(), UserClickListener {

    @Inject lateinit var githubService: GithubService
    @Inject lateinit var adapter: UserListAdapter

    private val _items = MutableLiveData<List<UserData>>()
    val items: LiveData<List<UserData>> = _items

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _showDetail = MutableLiveData<Event<User>>()
    val showDetail: LiveData<Event<User>> = _showDetail

    val bookmarks: LiveData<List<User>> = repository.allUsers.asLiveData()

    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0
    private var isLoading = false

    override fun onUserClick(position: Int) {
        val item = adapter[position] as User
        _showDetail.value = Event(item)
    }

    override fun onUserLongClick(position: Int) {
        val item = adapter[position]
    }

    override fun onBookmarkClick(position: Int) {
        val item = adapter[position] as User
        setBookmark(item)
    }

    fun loadNextPage(
        canScrollVertically: Boolean,
        lastVisibleItemPosition: Int,
        smoothScroller: LinearSmoothScroller,
        layoutManager: GridLayoutManager
    ) {
        if (canScrollVertically && lastVisibleItemPosition == adapter.itemCount - 1 && isLoading.not()) {
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
            githubService.getUserList(query = "june", perPage, currentPage)

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
        if (adapter.itemCount != 0) deleteProgress(progressPosition)
        lastPage = (users.total_count / perPage) + 1

        val newData = users.items.map {
            User(name = it.login, imageUrl = it.avatarUrl, description = "", isBookmark = false)
        }

        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            addAll(newData)
        }

        isLoading = false
    }

    private fun scrollToProgress(smoothScroller: LinearSmoothScroller, layoutManager: GridLayoutManager) {
        smoothScroller.targetPosition = adapter.itemCount
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun addProgress() {
        progressPosition = adapter.itemCount
        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            add(Progress)
        }
    }

    private fun deleteProgress(position: Int) {
        _items.value = (_items.value?.toMutableList() ?: mutableListOf()).apply {
            removeAt(position)
        }
    }

    // repository의 insert() 메서드를 호출하는 래퍼 insert() 메서드 -> insert() 구현이 UI에서 캡슐화
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }

    fun setBookmark(data: User) {
        val bookmarks = bookmarks.value ?: mutableListOf()

        if (data in bookmarks) {
            delete(data)
        } else {
            insert(data)
        }
    }
}