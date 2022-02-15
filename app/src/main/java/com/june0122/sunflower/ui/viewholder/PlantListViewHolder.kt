package com.june0122.sunflower.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.utils.PlantSelectedListener

class PlantListViewHolder(view: View, listener: PlantSelectedListener) : RecyclerView.ViewHolder(view) {
    private val plantNameTextView = itemView.findViewById<TextView>(R.id.tv_plant_name)
    private val availablePlantImageView = itemView.findViewById<ImageView>(R.id.img_available_plant)

    init {
        itemView.setOnClickListener { listener.onPlantSelected(absoluteAdapterPosition) }
    }

    fun bind(plant: Plant) {
        availablePlantImageView.load(plant.imageUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }
        plantNameTextView.text = plant.name
    }
}