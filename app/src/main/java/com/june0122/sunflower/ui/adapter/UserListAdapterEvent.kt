package com.june0122.sunflower.ui.adapter

import com.june0122.sunflower.model.data.UserData

interface UserListAdapterEvent {

    operator fun get(position: Int): UserData

    fun add(item: UserData)

    fun addAll(items: List<UserData>)

    fun remove(position: Int)

    fun clear()

    fun isEmpty(): Boolean

}