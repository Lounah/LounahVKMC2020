package com.lounah.vkmc.core.ui.bottomsheet

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lounah.vkmc.core.ui.R

val Dialog.bottomSheetView: ViewGroup
    get() = findViewById(R.id.design_bottom_sheet)

val Dialog.bottomSheetBehaviour: BottomSheetBehavior<ViewGroup>
    get() = BottomSheetBehavior.from(bottomSheetView)


fun Dialog.setState(state: Int) {
    bottomSheetBehaviour.state = state
}

fun Dialog.setStateChangeListener(listener: (Int) -> Unit) {
    bottomSheetBehaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, state: Int) = listener(state)
        override fun onSlide(bottomSheet: View, offset: Float) = Unit
    })
}
