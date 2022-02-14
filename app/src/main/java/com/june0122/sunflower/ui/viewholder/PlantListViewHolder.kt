package com.june0122.sunflower.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.utils.PlantSelectedListener

class PlantListViewHolder(view: View, listener: PlantSelectedListener): RecyclerView.ViewHolder(view) {
    private lateinit var plant: Plant
    private val plantNameTextView = itemView.findViewById<TextView>(R.id.tv_plant_name)

    init {
        itemView.setOnClickListener { listener.onPlantSelected(absoluteAdapterPosition) }
    }

    fun bind(plant: Plant) {
        this.plant = plant
        plantNameTextView.text = this.plant.name
    }
}