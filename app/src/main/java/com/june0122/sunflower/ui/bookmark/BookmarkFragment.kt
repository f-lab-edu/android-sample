package com.june0122.sunflower.ui.bookmark

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
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.databinding.FragmentBookmarkBinding
import com.june0122.sunflower.ui.list.UserListAdapter
import com.june0122.sunflower.ui.list.UserListFragment
import com.june0122.sunflower.ui.main.UserSharedViewModel
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.utils.decoration.UserListItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val bookmarkAdapter = UserListAdapter()
    private val viewModel: UserSharedViewModel by activityViewModels()
    private val layoutManager by lazy { GridLayoutManager(context, UserListFragment.DEFAULT_SPAN_COUNT) }
    private val clickListener by lazy {
        object : UserClickListener {
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

            override fun onBookmarkClick(position: Int) {
                val user = bookmarkAdapter[position] as User
                viewModel.delete(user)
//                viewModel.setBookmark(user)
            }
        }
    }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBookmarkBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView(layoutManager)
        viewModel.adapter = bookmarkAdapter
        viewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            bookmarkAdapter.updateUserListItems(viewModel.checkBookmarkPage(bookmarks))
        }
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
            bookmarkAdapter.listener = clickListener
            itemAnimator = null
            addItemDecoration(UserListItemDecoration(UserListFragment.DEFAULT_SPAN_COUNT, px, true))
        }
    }
}