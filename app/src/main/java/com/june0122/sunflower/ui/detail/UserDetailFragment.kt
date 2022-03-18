package com.june0122.sunflower.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.R
import com.june0122.sunflower.UsersApplication
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.databinding.FragmentUserDetailBinding
import com.june0122.sunflower.ui.list.UserListAdapter
import com.june0122.sunflower.ui.list.UserListViewModelFactory
import com.june0122.sunflower.ui.main.UserSharedViewModel
import com.june0122.sunflower.utils.UserClickListener

class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private var bookmarkStatus = false
    private lateinit var data: User

    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(object : UserClickListener {
            override fun onUserClick(position: Int) {
                viewModel.onUserClick(position)
            }

            override fun onUserLongClick(position: Int) {
                viewModel.onUserLongClick(position)
            }

            override fun onBookmarkClick(position: Int) {}
        })
    }

    private val viewModel: UserSharedViewModel by activityViewModels(
        factoryProducer = {
            UserListViewModelFactory(
                userListAdapter,
                (requireActivity().application as UsersApplication).repository
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: UserDetailFragmentArgs by navArgs()
        data = safeArgs.userData
        bookmarkStatus = safeArgs.bookmarkStatus
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentUserDetailBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPlantDetail.load(data.imageUrl) {
            crossfade(true)
            crossfade(300)
        }
        binding.tvPlantName.text = data.name
        binding.tvDescription.text = data.description

        val fab = binding.fabFavorite
        fab.setOnClickListener {
            if (fab.isSelected) {
                viewModel.delete(data)
                fab.bookmarkEvent(false, "Disable bookmark")
            } else {
                viewModel.insert(data)
                fab.bookmarkEvent(true, "Enable bookmark")
            }
        }

        viewModel.bookmarkStatus.observe(viewLifecycleOwner) { status ->
            if (status) fab.bookmarkEvent(true, R.drawable.ic_bookmark_filled)
            else fab.bookmarkEvent(false, R.drawable.ic_bookmark)
        }

        viewModel.checkBookmark(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FloatingActionButton.bookmarkEvent(status: Boolean, message: String) {
        this.isSelected = status
        viewModel.setBookmark(data)
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private fun FloatingActionButton.bookmarkEvent(status: Boolean, resId: Int) {
        this.isSelected = status
        setImageResource(resId)
    }
}