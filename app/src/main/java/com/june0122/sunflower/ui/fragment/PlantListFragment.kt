package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentPlantListBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.ui.adapter.VIEW_TYPE_LOADING
import com.june0122.sunflower.utils.EventObserver
import com.june0122.sunflower.utils.PlantClickListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration
import com.june0122.sunflower.utils.toast
import com.june0122.sunflower.viewmodel.PlantListViewModel
import com.june0122.sunflower.viewmodel.PlantListViewModelFactory

class PlantListFragment : Fragment() {
    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private lateinit var plantListAdapter: PlantListAdapter
    private val viewModel: PlantListViewModel by viewModels(
        factoryProducer = { PlantListViewModelFactory(plantListAdapter) }
    )

    private var spanCount = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        val view = binding.root

        // 뷰모델에 어댑터가 주입되어 있기 때문에 지연 초기화인 어댑터를 초기화 후에 ViewModel 내부의 필드 접근 가능
        plantListAdapter = PlantListAdapter(object : PlantClickListener {
            override fun onPlantClick(position: Int) {
                viewModel.onPlantClick(position)
            }

            override fun onPlantLongClick(position: Int) {
                viewModel.onPlantLongClick(position)
            }
        })

        viewModel.showDetail.observe(viewLifecycleOwner, EventObserver { plantData ->
            val plantDetailFragment = PlantDetailFragment.newInstance(plantData)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.container, plantDetailFragment)
                .addToBackStack(null)
                .commit()
        })

//        // 롱클릭 이벤트 재작성 필요
//        viewModel.showDetail.observe(viewLifecycleOwner) { plantData ->
//            PlantDialogFragment.newInstance(plantData).apply {
//                show(this@PlantListFragment.parentFragmentManager, DIALOG_PLANT)
//            }
//        }

        val layoutManager = GridLayoutManager(context, spanCount)
        setSpanSize(layoutManager)

        binding.rvPlantList.run {
            adapter = plantListAdapter
            this.layoutManager = layoutManager
            itemAnimator = null
            val px = resources.getDimensionPixelSize(R.dimen.margin_large)
            addItemDecoration(PlantListItemDecoration(spanCount, px, true))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val smoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_END
                        }
                    }

                    if (viewModel.canLoaded(
                            binding.rvPlantList.canScrollVertically(1).not(),
                            lastVisibleItemPosition
                        )
                    ) {
                        recyclerView.post {
                            viewModel.loadNextPage()
                            smoothScroller.targetPosition = plantListAdapter.itemCount
                            layoutManager.startSmoothScroll(smoothScroller)
                        }
                    }
                }
            })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserList()
        viewModel.statusMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message -> context.toast(message) }
        }

        binding.fabAddPlant.setOnClickListener {
            // 데이터를 직접 입력해서 아이템을 추가하는 식으로 변경 예정
            // api로부터 데이터를 받아오는 화면이기 때문에 여기서는 아이템을 추가할 필요 없음
            viewModel.items.add(
                Plant(
                    imageUrl = "https://avatars.githubusercontent.com/u/39554623?v=4",
                    name = "june0122",
                    description = "Junior Android Developer"
                )
            )
            plantListAdapter.notifyItemInserted(viewModel.items.size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (viewModel.checkItemType(position)) {
                    VIEW_TYPE_LOADING -> spanCount
                    else -> 1
                }
            }
        }
    }
}