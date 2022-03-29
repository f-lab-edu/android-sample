package com.june0122.sunflower.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class UserSharedViewModel @Inject constructor(private val repository: UserRepository) : ViewModel(), UserClickListener {

    init {
        viewModelScope.launch {
            repository.allUsers.onEach {
                _bookmarks.value = it
                checkBookmarks(it)
            }.launchIn(viewModelScope)
        }
    }

    @Inject
    lateinit var githubService: GithubService

    @Inject
    lateinit var adapter: UserListAdapter

    private val _items = MutableLiveData<List<UserData>>()
    val items: LiveData<List<UserData>> = _items

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _showDetail = MutableLiveData<Event<Int>>()
    val showDetail: LiveData<Event<Int>> = _showDetail

    private val _bookmarks = MutableLiveData<List<User>>()
    val bookmarks: LiveData<List<User>> = _bookmarks

    private val _currentItem = MutableLiveData<Event<User>>()
    val currentItem: LiveData<Event<User>> = _currentItem

    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0
    private var isLoading = false


    fun loadUserItem(position: Int) {
        (_items.value?.get(position) as? User)?.let {
            _currentItem.value = Event(it)
        }
    }

    override fun onUserClick(position: Int) {
        _showDetail.value = Event(position)
    }

    override fun onUserLongClick(position: Int) {
        val item = adapter[position]
    }

    override fun onBookmarkClick(position: Int) {
        (_items.value?.get(position) as? User)?.let {
            setBookmark(it)
        }
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

    private fun updateUserList(users: Users) = viewModelScope.launch {
        if (adapter.itemCount != 0) deleteProgress(progressPosition)
        lastPage = (users.total_count / perPage) + 1

        val newData = users.items.map {
            // isBookmark = false
            User(name = it.login, imageUrl = it.avatarUrl, description = "", isBookmark = repository.matcherName(it.login))
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

    fun setBookmark(data: User) = viewModelScope.launch {
        if (repository.matcherName(data.name)) {
            delete(data)
        } else {
            insert(data)
        }
    }

    private fun checkBookmarks(users: List<User>) {
//        Log.i("TEMP", "users $users")
//        Log.i("TEMP", "items.value ${_items.value}")

        _items.value = _items.value?.map {
            val user = it as User
//            Log.e("TEMP", "user $user")
            if (users.firstOrNull { it.name == user.name } != null) user.copy(isBookmark = true)
            else user.copy(isBookmark = false)
        }
//        Log.i("TEMP", "update items.value ${_items.value}")
    }

    fun checkBookmarkPage(bookmarkList: List<User>): List<User> {
        Log.d("Check", "Bookmark size : ${bookmarkList.size}")

        val tempList = bookmarkList.map { user ->
            user.copy(isBookmark = true)
        }

        tempList.forEach { user ->
            Log.d("Check", "${user.name} : ${user.isBookmark}")
        }

        return tempList
    }

//    fun checkUserListPage(users: List<UserData>): List<UserData> {
//        val bookmarks = bookmarks.value ?: mutableListOf()
//
//        val tempList = users.map { userData ->
//            if (bookmarks.contains(userData) && userData is User) {
//                userData.copy(isBookmark = true)
//            }
////            else if (!bookmarks.contains(userData) && userData is User) {
////                userData.copy(isBookmark = false)
////            }
//            else userData
//        }
//
//        return tempList
//    }

}