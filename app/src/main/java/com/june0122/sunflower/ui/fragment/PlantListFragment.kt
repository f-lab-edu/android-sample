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
import com.june0122.sunflower.utils.PlantSelectedListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration

class PlantListFragment : Fragment() {
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantListAdapter: PlantListAdapter

    // onCreateView(...)와 onViewCreated(...) 중 어디에 리사이클러뷰 및 어댑터 초기화를 해주는게 적절한가
    // ViewHolder나 Adapter를 다른 클래스 파일로 분리하지 않고 Fragment의 inner class로 선언하는 경우도 있던데 어떤 방법이 적절할까?
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_plant_list, container, false)

        plantRecyclerView = view.findViewById(R.id.rv_plant_list)
        plantRecyclerView.layoutManager = GridLayoutManager(context, 2)
        plantRecyclerView.addItemDecoration(PlantListItemDecoration(2, 60, true))
        plantListAdapter = PlantListAdapter(object : PlantSelectedListener {
            override fun onPlantSelected(position: Int) {
                val plantData = plantListAdapter.items[position]
                val plantDetailFragment = PlantDetailFragment.newInstance(plantData)

                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, plantDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        plantRecyclerView.adapter = plantListAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}