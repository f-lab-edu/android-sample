package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.databinding.FragmentPlantDetailBinding
import com.june0122.sunflower.model.data.Plant
import com.june0122.sunflower.ui.adapter.PlantListAdapter
import com.june0122.sunflower.utils.PlantClickListener
import com.june0122.sunflower.viewmodel.PlantListViewModelFactory
import com.june0122.sunflower.viewmodel.SharedViewModel

class PlantDetailFragment : Fragment() {
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: Plant

    private val plantListAdapter: PlantListAdapter by lazy {
        PlantListAdapter(object : PlantClickListener {
            override fun onPlantClick(position: Int) {
                viewModel.onPlantClick(position)
            }

            override fun onPlantLongClick(position: Int) {
                viewModel.onPlantLongClick(position)
            }
        })
    }

    private val viewModel: SharedViewModel by activityViewModels(
        factoryProducer = { PlantListViewModelFactory(plantListAdapter) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: PlantDetailFragmentArgs by navArgs()
        data = safeArgs.userData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentPlantDetailBinding.inflate(inflater, container, false).also {
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
        binding.fabFavorite.setOnClickListener { fab ->
            viewModel.addBookmark(data)

            Snackbar.make(fab, "Added to bookmark", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.anchorId = View.NO_ID
            fab.layoutParams = layoutParams
            fab.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}