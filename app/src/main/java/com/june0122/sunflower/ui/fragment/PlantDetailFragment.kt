package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant

class PlantDetailFragment : Fragment() {
    lateinit var data: Plant

    companion object {
        fun newInstance() = PlantDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantName = view.findViewById<TextView>(R.id.plantNameTextView)
        plantName.text = data.name

        val description = view.findViewById<TextView>(R.id.descriptionTextView)
        description.text = data.description
    }

    fun receivePlantData(data: Plant) {
        this.data = data
    }
}