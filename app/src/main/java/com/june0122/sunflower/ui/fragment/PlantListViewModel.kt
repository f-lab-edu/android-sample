package com.june0122.sunflower.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.PlantClickListener

class PlantListViewModel(
    private val plantListAdapter: PlantListAdapter,
) : ViewModel(), PlantClickListener {

    private val _showDetail = MutableLiveData<Plant>()
    val showDetail: LiveData<Plant> = _showDetail

    override fun onPlantClick(position: Int) {
        val item = plantListAdapter.items[position]
        _showDetail.value = item
    }

    override fun onPlantLongClick(position: Int) {
        TODO("Not yet implemented")
    }
}