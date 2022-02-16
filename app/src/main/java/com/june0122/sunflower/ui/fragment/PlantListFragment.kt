package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration

class PlantListFragment : Fragment() {
    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantListAdapter: PlantListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root

        plantRecyclerView = binding.rvPlantList
        plantRecyclerView.layoutManager = GridLayoutManager(context, 2)
        plantRecyclerView.addItemDecoration(PlantListItemDecoration(2, 60, true))
        plantListAdapter = PlantListAdapter { position ->
            val plantData = plantListAdapter.items[position]
            val plantDetailFragment = PlantDetailFragment.newInstance(plantData)

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, plantDetailFragment)
                .addToBackStack(null)
                .commit()
        }
        plantRecyclerView.adapter = plantListAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}