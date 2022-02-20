package com.june0122.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.june0122.sunflower.databinding.FragmentPlantDetailBinding
import com.june0122.sunflower.model.data.Plant

private const val PLANT_DATA = "plant_data"

class PlantDetailFragment : Fragment() {
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: Plant

    companion object {
        fun newInstance(plantData: Plant): PlantDetailFragment {
            val args = bundleOf(
                PLANT_DATA to plantData
            )

            return PlantDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getParcelable<Plant>(PLANT_DATA) as Plant
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