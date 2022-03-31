package com.june0122.sunflower.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentUserListBinding
import com.june0122.sunflower.ui.list.UserListAdapter.Companion.VIEW_TYPE_LOADING
import com.june0122.sunflower.ui.main.UserSharedViewModel
import com.june0122.sunflower.utils.EventObserver
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.decoration.UserListItemDecoration
import com.june0122.sunflower.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val userListAdapter: UserListAdapter = UserListAdapter()
    private val viewModel: UserSharedViewModel by activityViewModels()
    private val layoutManager by lazy { GridLayoutManager(context, DEFAULT_SPAN_COUNT) }
    private val clickListener by lazy {
        object : UserClickListener {
            override fun onUserClick(position: Int) {
                viewModel.onUserClick(position)
            }

            override fun onUserLongClick(position: Int) {
                viewModel.onUserLongClick(position)
            }

            override fun onBookmarkClick(position: Int) {
                viewModel.onBookmarkClick(position)
            }
        }
    }
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
        return FragmentUserListBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView(layoutManager)
        setSpanSize(layoutManager)
        viewModel.adapter = userListAdapter

        if (userListAdapter.itemCount == 0) viewModel.getUserList()

        viewModel.items.observe(requireActivity()) { users ->
            if (users != null) userListAdapter.updateUserListItems(users)
        }

        viewModel.statusMessage.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { message -> context.toast(message) }
        }

        viewModel.showDetail.observe(requireActivity(), EventObserver { userData ->
            val action = UserListFragmentDirections.detailAction(userData)
            findNavController().navigate(action)
        })
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
            userListAdapter.listener = clickListener
            itemAnimator = null
            addItemDecoration(UserListItemDecoration(DEFAULT_SPAN_COUNT, px, true))
            addOnScrollListener(scrollListener)
        }
    }

    companion object {
        const val DEFAULT_SPAN_COUNT = 2
    }
}