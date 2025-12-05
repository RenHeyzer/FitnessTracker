package com.azim.fitness.utils

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.azim.fitness.R

fun Fragment.loadingDialog(): Dialog {
    val dialog = Dialog(requireContext())
    dialog.setContentView(R.layout.dialog_loading)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCancelable(false)
    return dialog
}