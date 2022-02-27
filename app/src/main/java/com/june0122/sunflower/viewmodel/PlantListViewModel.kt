package com.june0122.sunflower.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.june0122.sunflower.api.GithubService
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.Users
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.ui.adapter.STATUS_LOADING
import com.june0122.sunflower.ui.adapter.VIEW_TYPE_ITEM
import com.june0122.sunflower.ui.adapter.VIEW_TYPE_LOADING
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
    private val _showDetail = MutableLiveData<Event<Plant>>()
    val showDetail: LiveData<Event<Plant>> = _showDetail

    val items = plantListAdapter.items

    private var itemCount = 0
    private var currentPage = 1
    private var perPage = 20
    private var lastPage = 0

    override fun onPlantClick(position: Int) {
        val item = plantListAdapter.items[position]
        _showDetail.value = Event(item)
//        _navigateToDetail.value = Event(true)
//        _navigateToDetail.postValue(Event(true)) // postValue vs setValue
    }

    override fun onPlantLongClick(position: Int) {
        val item = items[position]
    }

    fun checkItemType(position: Int): Int {
        return if (position == itemCount) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun canLoaded(canScrollVertically: Boolean, lastVisibleItemPosition: Int): Boolean =
        canScrollVertically && lastVisibleItemPosition == itemCount

    fun loadNextPage() {
        deleteProgress()
        currentPage++
        if (currentPage <= lastPage) getUserList()
    }

    private fun deleteProgress() {
        if (items.last().description == STATUS_LOADING) {
            items.removeAt(items.lastIndex)
            plantListAdapter.notifyItemRemoved(items.lastIndex + 1)
        }
    }

    fun getUserList() {
        val userListCall: Call<Users> =
            GithubService.create().getUserList(query = "june", perPage, currentPage)

        userListCall.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    response.body()?.let { users -> updateUserList(users) }
                } else if (response.code() == 403) {
                    // Toast.makeText(context, "API rate limit exceeded.", Toast.LENGTH_SHORT).show()
                    _statusMessage.value = Event("API rate limit exceeded")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
//                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                _statusMessage.value = Event(t.localizedMessage)
                Log.e("PlantList", "XXX - ${t.localizedMessage}")
            }
        })
    }

    private fun updateUserList(users: Users) {
        lastPage = (users.total_count / perPage) + 1

        users.items.forEach {
            items.add(
                Plant(imageUrl = it.avatarUrl, name = it.login, description = "")
            )
        }

        plantListAdapter.notifyItemRangeInserted(itemCount, users.items.size)
        itemCount += users.items.size

        if (currentPage < lastPage) {
            items.add(Plant("", "", STATUS_LOADING)) // progressbar 보여주기 위한 아이템 1개 추가
            plantListAdapter.notifyItemInserted(itemCount + 1)
        }
    }
}