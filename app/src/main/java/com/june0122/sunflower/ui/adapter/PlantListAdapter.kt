package com.june0122.sunflower.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.databinding.ItemPlantListBinding
import com.june0122.sunflower.databinding.ItemProgressBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.model.data.PlantData
import com.june0122.sunflower.model.data.Progress
import com.june0122.sunflower.ui.viewholder.PlantListViewHolder
import com.june0122.sunflower.ui.viewholder.ProgressHolder
import com.june0122.sunflower.utils.PlantClickListener

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class PlantListAdapter(private val listener: PlantClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val _items = mutableListOf<PlantData>()
    val items get() = _items

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is Progress -> VIEW_TYPE_LOADING
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

    override fun getItemCount(): Int = _items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = items[holder.absoluteAdapterPosition]) {
            is Plant -> if (holder is PlantListViewHolder) holder.bind(data)
            is Progress -> {}
        }
    }
}