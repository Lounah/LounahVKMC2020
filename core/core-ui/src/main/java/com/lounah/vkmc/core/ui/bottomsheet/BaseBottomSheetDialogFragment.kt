package com.lounah.vkmc.core.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lounah.vkmc.core.ui.R

abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layout: Int,
    private val expandByDefault: Boolean = true
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme_Dark)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onStart() {
        super.onStart()
        if (expandByDefault)
            dialog?.setState(BottomSheetBehavior.STATE_EXPANDED)
    }

    protected fun argsString(key: String): String {
        return arguments?.getString(key) ?: error("Arguments is null or key doesn't present")
    }
}
