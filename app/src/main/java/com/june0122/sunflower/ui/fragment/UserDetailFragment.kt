package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.june0122.sunflower.databinding.FragmentUserDetailBinding
import com.june0122.sunflower.model.data.User
import com.june0122.sunflower.ui.adapter.UserListAdapter
import com.june0122.sunflower.utils.UserClickListener
import com.june0122.sunflower.viewmodel.SharedViewModel
import com.june0122.sunflower.viewmodel.UserListViewModelFactory

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
        })
    }

    private val viewModel: SharedViewModel by activityViewModels(
        factoryProducer = { UserListViewModelFactory(userListAdapter) }
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
        binding.fabFavorite.setOnClickListener {
            viewModel.setBookmark(binding.fabFavorite, data)
        }

        viewModel.checkBookmark(binding.fabFavorite, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}