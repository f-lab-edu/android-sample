package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant

private const val PLANT_DATA = "plant_data"

class PlantDetailFragment : Fragment() {
    lateinit var data: Plant

    companion object {
        fun newInstance(plantData: Plant): PlantDetailFragment {
            val args = Bundle().apply {
                putSerializable(PLANT_DATA, plantData)
            }

            return PlantDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getSerializable(PLANT_DATA) as Plant
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_plant_name).run {
            text = data.name
        }

        view.findViewById<TextView>(R.id.tv_description).run {
            text = data.description
        }
    }
}