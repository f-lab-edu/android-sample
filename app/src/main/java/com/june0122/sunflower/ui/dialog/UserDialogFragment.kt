package com.june0122.sunflower.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.june0122.sunflower.R
import com.june0122.sunflower.data.entity.User

class UserDialogFragment : DialogFragment() {

    companion object {
        private const val PLANT_DATA = "plant_data"
        fun newInstance(userData: User): UserDialogFragment {
            val args = bundleOf(
                PLANT_DATA to userData
            )

            return UserDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val plantData = arguments?.getParcelable<User>(PLANT_DATA)
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