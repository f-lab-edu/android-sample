package com.june0122.sunflower.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
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

class PlantListViewModel(
    private val plantListAdapter: PlantListAdapter,
) : ViewModel(), PlantClickListener {
    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage
    private val _showDetail = MutableLiveData<Event<PlantData>>()
    val showDetail: LiveData<Event<PlantData>> = _showDetail

    val items = plantListAdapter.items

    private var itemCount = 0
    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0
    private var progressPosition = 0

    override fun onPlantClick(position: Int) {
        val item = plantListAdapter.items[position]
        _showDetail.value = Event(item)
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
        items.addAll(newData)
        plantListAdapter.notifyItemRangeInserted(itemCount, users.items.size)
        itemCount += users.items.size
    }

    private fun scrollToProgress(smoothScroller: LinearSmoothScroller, layoutManager: GridLayoutManager) {
        smoothScroller.targetPosition = plantListAdapter.itemCount
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun addProgress() {
        plantListAdapter.items.add(Progress)
        plantListAdapter.notifyItemInserted(progressPosition)
    }

    private fun deleteProgress(position: Int) {
        items.removeAt(position)
        plantListAdapter.notifyItemRemoved(position)
    }
}