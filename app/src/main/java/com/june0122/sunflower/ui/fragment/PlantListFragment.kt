package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration

class PlantListFragment : Fragment() {

    companion object {
        fun newInstance() = PlantListFragment()
    }

    private val plantListAdapter = PlantListAdapter()
//    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_plant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantListLayoutManager = GridLayoutManager(context, 2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.plantListRecyclerView).apply {
            adapter = plantListAdapter
            layoutManager = plantListLayoutManager
            addItemDecoration(PlantListItemDecoration(2, 60, true))
        }
        plantListLayoutManager.orientation = GridLayoutManager.VERTICAL
    }
}