package com.june0122.sunflower.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentUserListBinding
import com.june0122.sunflower.ui.list.UserListAdapter.Companion.VIEW_TYPE_LOADING
import com.june0122.sunflower.utils.EventObserver
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.decoration.UserListItemDecoration
import com.june0122.sunflower.utils.toast

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(object : UserClickListener {
            override fun onUserClick(position: Int) {
                viewModel.onUserClick(position)
            }

            override fun onUserLongClick(position: Int) {
                viewModel.onUserLongClick(position)
            }

            override fun onBookmarkClick(position: Int) {
                viewModel.onBookmarkClick(position)
//                viewModel.checkBookmark(position, userListAdapter[position] as User)
            }
        })
    }

    private val viewModel: UserSharedViewModel by activityViewModels(
        factoryProducer = { UserListViewModelFactory(userListAdapter) }
    )

    private val layoutManager by lazy { GridLayoutManager(context, DEFAULT_SPAN_COUNT) }

    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val smoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int = SNAP_TO_END
                }
                recyclerView.post {
                    viewModel.loadNextPage(
                        recyclerView.canScrollVertically(1).not(),
                        lastVisibleItemPosition,
                        smoothScroller,
                        layoutManager
                    )
                }
            }
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.showDetail.observe(requireActivity(), EventObserver { plantData ->
            val action = UserListFragmentDirections.detailAction(plantData)
            findNavController().navigate(action)
        })

//        // 롱클릭 이벤트 재작성 필요
//        viewModel.showDetail.observe(viewLifecycleOwner) { plantData ->
//            PlantDialogFragment.newInstance(plantData).apply {
//                show(this@PlantListFragment.parentFragmentManager, DIALOG_PLANT)
//            }
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView(layoutManager)
        setSpanSize(layoutManager)

        if (userListAdapter.itemCount == 0) {
            viewModel.getUserList()
        }

        viewModel.items.observe(requireActivity()) {
            userListAdapter.updateUserListItems(it)
        }

        viewModel.statusMessage.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { message -> context.toast(message) }
        }

//        viewModel.bookmarks.observe(requireActivity(), EventObserver { bookmarks ->
//        })

        viewModel.bookmarkStatus.observe(viewLifecycleOwner) { status ->
            viewModel.bookmarkPosition.observe(viewLifecycleOwner) { position ->
                val viewHolder = binding.rvPlantList.findViewHolderForAdapterPosition(position)
                val bookmarkButton = viewHolder?.itemView?.findViewById<ImageView>(R.id.btn_bookmark)
                bookmarkButton?.isSelected = status.not()

                Log.e("Check", "POS - $position")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.layoutManager = null
        recyclerView.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private fun setSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (userListAdapter.getItemViewType(position)) {
                    VIEW_TYPE_LOADING -> DEFAULT_SPAN_COUNT
                    else -> 1
                }
            }
        }
    }

    private fun configureRecyclerView(layoutManager: GridLayoutManager) {
        val px = resources.getDimensionPixelSize(R.dimen.margin_large)

        recyclerView = binding.rvPlantList.apply {
            this.layoutManager = layoutManager
            adapter = userListAdapter
            itemAnimator = null
            addItemDecoration(UserListItemDecoration(DEFAULT_SPAN_COUNT, px, true))
            addOnScrollListener(scrollListener)
        }
    }

    companion object {
        const val DEFAULT_SPAN_COUNT = 2
    }
}