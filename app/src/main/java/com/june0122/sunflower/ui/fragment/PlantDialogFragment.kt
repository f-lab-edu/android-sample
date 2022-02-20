package com.june0122.sunflower.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.june0122.sunflower.R
import com.june0122.sunflower.model.data.Plant

class PlantDialogFragment : DialogFragment() {

    companion object {
        private const val PLANT_DATA = "plant_data"
        fun newInstance(plantData: Plant): PlantDialogFragment {
            val args = bundleOf(
                PLANT_DATA to plantData
            )

            return PlantDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val plantData = arguments?.getParcelable<Plant>(PLANT_DATA)
        return activity?.let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle("${plantData?.name}")
                setItems(R.array.plant_dialog_array) { dialog, which ->
                    when (which) {
                        0 -> {
                            Log.d("Dialog", "Bookmark")
                        }
                        1 -> {
                            Log.d("Dialog", "Edit")
                        }
                        2 -> {
                            Log.d("Dialog", "Remove")
                        }
                    }
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}