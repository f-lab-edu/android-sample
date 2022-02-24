package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.PlantClickListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration
import com.june0122.sunflower.utils.toast
import com.june0122.sunflower.viewmodel.PlantListViewModel
import com.june0122.sunflower.viewmodel.PlantListViewModelFactory

private const val DIALOG_PLANT = "DialogPlant"

class PlantListFragment : Fragment() {
    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantListAdapter: PlantListAdapter
    private val viewModel: PlantListViewModel by viewModels(
        factoryProducer = { PlantListViewModelFactory(plantListAdapter) }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root

        // 뷰모델에 어댑터가 주입되어 있기 때문에 지연 초기화인 어댑터를 초기화 후에 ViewModel 내부의 필드 접근 가능
        plantListAdapter = PlantListAdapter(object : PlantClickListener {
            override fun onPlantClick(position: Int) {
                viewModel.onPlantClick(position)
                viewModel.showDetail.observe(viewLifecycleOwner) { plantData ->
                    val plantDetailFragment = PlantDetailFragment.newInstance(plantData)
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, plantDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onPlantLongClick(position: Int) {
                viewModel.onPlantClick(position)
                viewModel.showDetail.observe(viewLifecycleOwner) { plantData ->
                    PlantDialogFragment.newInstance(plantData).apply {
                        show(this@PlantListFragment.parentFragmentManager, DIALOG_PLANT)
                    }
                }
            }
        })

        val layoutManager = GridLayoutManager(context, viewModel.spanCount)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == viewModel.itemCount) layoutManager.spanCount else 1
            }
        }

        plantRecyclerView = binding.rvPlantList
        plantRecyclerView.adapter = plantListAdapter
        plantRecyclerView.layoutManager = layoutManager
        plantRecyclerView.addItemDecoration(PlantListItemDecoration(viewModel.spanCount, 60, true))
        plantRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() { // < ---- 액션의 등록 및 시작
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (canLoaded(
                        binding.rvPlantList.canScrollVertically(1).not(),
                        lastVisibleItemPosition,
                        viewModel.itemCount
                    )
                ) {
                    recyclerView.post { viewModel.deleteProgress() }
                    viewModel.currentPage++
                    if (viewModel.currentPage <= viewModel.lastPage) viewModel.getUserList()
                }
            }
        })

        return view
    }

    private fun canLoaded(canScrollVertically: Boolean, lastVisibleItemPosition: Int, totalCount: Int): Boolean =
        canScrollVertically && lastVisibleItemPosition == totalCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserList()
        viewModel.message.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                context.toast(message)
            }
        }

        binding.fabAddPlant.setOnClickListener {
            val items = viewModel.items
            // 데이터를 직접 입력해서 아이템을 추가하는 식으로 변경 예정
            items.add(
                Plant(
                    imageUrl = "https://avatars.githubusercontent.com/u/39554623?v=4",
                    name = "june0122",
                    description = "Junior Android Developer"
                )
            )
            plantListAdapter.notifyItemInserted(items.size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}