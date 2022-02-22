package com.june0122.sunflower.ui.fragment

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlantViewModel : ViewModel() {

    private val _success = MutableLiveData("")
    val success: LiveData<String> = _success

    private val _fail = MutableLiveData("")
    val fail: LiveData<String> = _fail

    private val _likeSuccess = MutableLiveData(false)
    val likeSuccess: LiveData<Boolean> = _likeSuccess

    private val _likeItem = MutableLiveData<ClipData.Item>()

    fun load() {
        _success.value = "성공"
        _fail.value = "실패"
    }

    fun like() {
        // ...
        _likeSuccess.value = true
    }
}