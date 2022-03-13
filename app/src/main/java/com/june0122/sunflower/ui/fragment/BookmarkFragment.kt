package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.util.Log
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
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.ui.adapter.UserListAdapter
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.decoration.UserListItemDecoration
import com.june0122.sunflower.viewmodel.UserSharedViewModel
import com.june0122.sunflower.viewmodel.UserListViewModelFactory

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserSharedViewModel by activityViewModels(
        factoryProducer = { UserListViewModelFactory(bookmarkAdapter) }
    )

    private val bookmarkAdapter: UserListAdapter by lazy {
        UserListAdapter(object : UserClickListener {
            override fun onUserClick(position: Int) {
                val item = bookmarkAdapter[position]
                val action = BookmarkFragmentDirections.detailAction(
                    userData = item as User,
                    bookmarkStatus = true
                )
                findNavController().navigate(action)
            }

            override fun onUserLongClick(position: Int) {
                val item = bookmarkAdapter[position]
            }
        })
    }

    private val layoutManager by lazy { GridLayoutManager(context, UserListFragment.DEFAULT_SPAN_COUNT) }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.bookmarks.observe(viewLifecycleOwner) {
            bookmarkAdapter.addAll(it)
            bookmarkAdapter.submitList(it)
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
            addItemDecoration(UserListItemDecoration(UserListFragment.DEFAULT_SPAN_COUNT, px, true))
        }
    }
}