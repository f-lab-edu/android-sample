package com.june0122.sunflower.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.ItemPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.utils.PlantSelectedListener

class PlantListViewHolder(binding: ItemPlantListBinding, listener: PlantSelectedListener) :
    RecyclerView.ViewHolder(binding.root) {
    private val plantNameTextView = binding.tvPlantName
    private val availablePlantImageView = binding.imgAvailablePlant

    init {
        itemView.setOnClickListener { listener.onPlantSelected(absoluteAdapterPosition) }
    }

    fun bind(plant: Plant) {
        availablePlantImageView.load(plant.imageUrl) {
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }
        plantNameTextView.text = plant.name
    }
}