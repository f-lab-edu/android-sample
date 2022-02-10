package com.june0122.sunflower.ui.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.viewholder.PlantListViewHolder

class PlantListAdapter : RecyclerView.Adapter<PlantListViewHolder>() {
    var items = arrayListOf<Plant>(
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
        Plant(image = "", name = "Apple"),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder = PlantListViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {
            val plantName = findViewById<TextView>(R.id.plantNameTextView)
            plantName.text = model.name
        }
    }
}