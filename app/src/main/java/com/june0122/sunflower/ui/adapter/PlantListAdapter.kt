package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.databinding.ItemPlantListBinding
import com.june0122.sunflower.databinding.ItemProgressBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.viewholder.PlantListViewHolder
import com.june0122.sunflower.ui.viewholder.ProgressHolder
import com.june0122.sunflower.utils.PlantClickListener

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1
const val STATUS_LOADING = "STATUS_LOADING"

class PlantListAdapter(private val listener: PlantClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 1. item은 언제든 add, remove, clear 할 수 있어야 한다.
     * 2. item 리스트 변수는 private으로 선언
     * 3. item이 추가되었을 경우 리스트 갱신이 동작하도록 구현
     */
    val items = mutableListOf<Plant>()

    override fun getItemViewType(position: Int): Int {
        return when(items[position].description) {
            STATUS_LOADING -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ITEM -> {
                val binding =
                    ItemPlantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PlantListViewHolder(binding, listener)
            }
            else -> {
                val binding =
                    ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProgressHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = items[holder.absoluteAdapterPosition]

        if (holder is PlantListViewHolder) holder.bind(plant)
    }

    fun deleteProgress() {
        if (items.last().description == STATUS_LOADING) {
            items.removeAt(items.lastIndex)
        }
    }
}