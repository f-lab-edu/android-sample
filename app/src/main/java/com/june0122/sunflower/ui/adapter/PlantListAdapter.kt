package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.databinding.ItemPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.viewholder.PlantListViewHolder
import com.june0122.sunflower.utils.PlantClickListener

class PlantListAdapter(private val listener: PlantClickListener) : RecyclerView.Adapter<PlantListViewHolder>() {
    /**
     * 1. item은 언제든 add, remove, clear 할 수 있어야 한다.
     * 2. item 리스트 변수는 private으로 선언
     * 3. item이 추가되었을 경우 리스트 갱신이 동작하도록 구현
     */
    val items = mutableListOf<Plant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder {
        val binding =
            ItemPlantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlantListViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        val plant = items[holder.absoluteAdapterPosition]

        holder.bind(plant)
    }
}