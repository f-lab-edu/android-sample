package com.june0122.sunflower.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.PlantData
import com.june0122.sunflower.model.data.Progress
import com.june0122.sunflower.model.data.Users
import com.june0122.sunflower.network.RetrofitClientInstance
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.Event
import com.june0122.sunflower.utils.PlantClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel(
    private val plantListAdapter: PlantListAdapter,
) : ViewModel(), PlantClickListener {
    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _showDetail = MutableLiveData<Event<Plant>>()
    val showDetail: LiveData<Event<Plant>> = _showDetail

    private val _items = MutableLiveData<List<PlantData>>()
    val items: LiveData<List<PlantData>> = _items

    private val bookmarkList = mutableListOf<Plant>()
    private val _bookmarks = MutableLiveData<List<PlantData>>()
    val bookmarks: LiveData<List<PlantData>> = _bookmarks

    private var itemCount = 0
    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0

    override fun onPlantClick(position: Int) {
        val item = plantListAdapter.items[position]
        _showDetail.value = Event(item as Plant)
    }

    override fun onPlantLongClick(position: Int) {
        val item = plantListAdapter.items[position]
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
            Plant(imageUrl = it.avatarUrl, name = it.login, description = "")
        }
        _items.postValue(plantListAdapter.items.apply { addAll(newData) })
        plantListAdapter.notifyItemRangeInserted(itemCount, users.items.size)
        itemCount += users.items.size
    }

    private fun scrollToProgress(smoothScroller: LinearSmoothScroller, layoutManager: GridLayoutManager) {
        smoothScroller.targetPosition = plantListAdapter.itemCount
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun addProgress() {
        _items.value = plantListAdapter.items.apply { add(Progress) }
        plantListAdapter.notifyItemInserted(progressPosition)
    }

    private fun deleteProgress(position: Int) {
        _items.value = plantListAdapter.items.apply { removeAt(position) }
        plantListAdapter.notifyItemRemoved(position)
    }

    fun setBookmark(fab: FloatingActionButton, data: Plant) {
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

    fun checkBookmark(fab: FloatingActionButton, data: Plant) {
        if (data in bookmarkList) {
            fab.setImageResource(R.drawable.ic_bookmark_filled)
        }
    }
}