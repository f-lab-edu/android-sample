package com.june0122.sunflower.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.FragmentUserDetailBinding
import com.june0122.sunflower.ui.main.UserSharedViewModel
import com.june0122.sunflower.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private var bookmarkStatus = false
    private var position: Int = -1
    private lateinit var snackBarMessage: String

    private val viewModel: UserSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: UserDetailFragmentArgs by navArgs()
        position = safeArgs.position
        bookmarkStatus = safeArgs.bookmarkStatus
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentUserDetailBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (position == -1) return

        viewModel.loadUserItem(position)

        viewModel.currentItem.observe(viewLifecycleOwner, EventObserver { data ->
            binding.imgPlantDetail.load(data.imageUrl) {
                crossfade(true)
                crossfade(300)
            }
            binding.tvPlantName.text = data.name
            binding.tvDescription.text = data.description

            val fab = binding.fabFavorite
            if (data.isBookmark) fab.setImageResource(R.drawable.ic_bookmark_filled)
            fab.setOnClickListener {
                viewModel.setBookmark(data)
                Snackbar.make(fab, snackBarMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }

            viewModel.bookmarks.observe(requireActivity()) { bookmarks ->
//                snackBarMessage = bookmarks.firstOrNull { it.name == data.name }?.let {
//                    fab.setImageResource(R.drawable.ic_bookmark_filled)
//                    "Disable Bookmark"
//                } ?: run {
//                    fab.setImageResource(R.drawable.ic_bookmark)
//                    "Enable Bookmark"
//                }
                snackBarMessage = if (bookmarks.firstOrNull { it.name == data.name } != null) {
                    fab.setImageResource(R.drawable.ic_bookmark_filled)
                    "Disable Bookmark"
                } else {
                    fab.setImageResource(R.drawable.ic_bookmark)
                    "Enable Bookmark"
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}