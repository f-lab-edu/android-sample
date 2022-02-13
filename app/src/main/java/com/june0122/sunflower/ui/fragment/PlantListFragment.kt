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
import com.june0122.sunflower.utils.ListItemClickListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration

class PlantListFragment : Fragment() {
    companion object {
        fun newInstance() = PlantListFragment() // 동반 객체의 newInstance() 사용 시의 이점은?
    }

    private val plantListAdapter = PlantListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_plant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantListLayoutManager = GridLayoutManager(context, 2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_plant_list).apply {
            adapter = plantListAdapter
            layoutManager = plantListLayoutManager
            addItemDecoration(PlantListItemDecoration(2, 60, true))
        }
        plantListLayoutManager.orientation = GridLayoutManager.VERTICAL

        recyclerView.addOnItemTouchListener(
            ListItemClickListener(
                view.context,
                recyclerView,
                object : ListItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val plantDetailFragment = PlantDetailFragment()
                        val plantData = plantListAdapter.items[position]

                        plantDetailFragment.receivePlantData(plantData)
                        activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.container, plantDetailFragment)
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            )
        )

    }
}