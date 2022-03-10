package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentBookmarkBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.PlantClickListener
import com.june0122.sunflower.utils.decoration.PlantListItemDecoration
import com.june0122.sunflower.viewmodel.PlantListViewModelFactory
import com.june0122.sunflower.viewmodel.SharedViewModel

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels(
        factoryProducer = { PlantListViewModelFactory(bookmarkAdapter) }
    )

    private val bookmarkAdapter: PlantListAdapter by lazy {
        PlantListAdapter(object : PlantClickListener {
            override fun onPlantClick(position: Int) {
                val item = bookmarkAdapter.items[position]
                val action = BookmarkFragmentDirections.detailAction(
                    userData = item as Plant,
                    bookmarkStatus = true
                )
                findNavController().navigate(action)
            }

            override fun onPlantLongClick(position: Int) {
                val item = bookmarkAdapter.items[position]
            }
        })
    }

    private val layoutManager by lazy { GridLayoutManager(context, PlantListFragment.DEFAULT_SPAN_COUNT) }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.bookmarks.observe(viewLifecycleOwner) {
            // 북마크 화면에서 아이템을 클릭 후 되돌아올때 이벤트 감지하므로 모두 지우고 추가 (더 나은 방법 고민해보기)
            bookmarkAdapter.items.clear()
            bookmarkAdapter.items.addAll(it)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView(layoutManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.layoutManager = null
        _binding = null
    }

    private fun configureRecyclerView(layoutManager: GridLayoutManager) {
        val px = resources.getDimensionPixelSize(R.dimen.margin_large)
        recyclerView = binding.rvBookmarkList.apply {
            this.layoutManager = layoutManager
            adapter = bookmarkAdapter
            itemAnimator = null
            addItemDecoration(PlantListItemDecoration(PlantListFragment.DEFAULT_SPAN_COUNT, px, true))
        }
    }
}