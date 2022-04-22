package com.june0122.sunflower.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.databinding.FragmentUserDetailBinding
import com.june0122.sunflower.ui.main.UserSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private lateinit var binding: FragmentUserDetailBinding
    private var bookmarkStatus = false
    private lateinit var data: User
    private lateinit var snackBarMessage: String

    private val viewModel: UserSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: UserDetailFragmentArgs by navArgs()
        data = safeArgs.userData
        bookmarkStatus = safeArgs.bookmarkStatus
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            this.user = data

            // TODO: Binding 함수로 분리
            fabFavorite.setOnClickListener {
                data = if (data.isBookmark) {
                    snackBarMessage = "Disable Bookmark"
                    data.copy(isBookmark = false)
                } else {
                    snackBarMessage = "Enable Bookmark"
                    data.copy(isBookmark = true)
                }
                this.user = data
                viewModel.setBookmark(data)
                Snackbar.make(fabFavorite, snackBarMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }
        }
    }
}