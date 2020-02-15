package com.lounah.vkmc.core.ui.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.view.Gravity.BOTTOM
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import java.lang.Math.max

fun Dialog.attachStickyFooter(stickyView: View,
                              doOnShow: () -> Unit = {},
                              doOnDismiss: () -> Unit = {}) {
    check(this is AppCompatDialog) {
        "targetDialog must be inherited from AppCompatDialog. Use AndroidX!"
    }
    val onDismiss = {
        doOnDismiss()
        dismiss()
    }

    setOnShowListener(StickyFooterDialogHelper(this, stickyView, doOnShow, onDismiss))
}

private const val STICKY_VIEW_DISMISS_MULTIPLIER = 1.4f

class StickyFooterDialogHelper(private val target: Dialog,
                               private val stickyView: View,
                               private val onShow: () -> Unit,
                               private val dismissCallback: () -> Unit)
    : DialogInterface.OnShowListener {

    private val slideCallback: (View, Float) -> Unit = { _, offset ->
        val newOffset = -offset * stickyView.height * STICKY_VIEW_DISMISS_MULTIPLIER
        stickyView.translationY = max(newOffset, 0f)
    }

    override fun onShow(dialog: DialogInterface) {
        val dialogView = target.findViewById<ViewGroup>(R.id.design_bottom_sheet)

        val containerLayout = target.findViewById(R.id.container) as ViewGroup
        val parent = stickyView.parent as ViewGroup

        parent.removeView(stickyView)

        stickyView.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, BOTTOM)
        containerLayout.addView(stickyView, containerLayout.childCount)

        setBottomSheetCallback(dialogView, slideCallback, dismissCallback, onShow)
    }
}

private fun setBottomSheetCallback(bottomSheet: ViewGroup,
                                   slide: (View, Float) -> Unit,
                                   dismiss: () -> Unit,
                                   show: () -> Unit) {
    BottomSheetBehavior.from(bottomSheet)
        .setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, offset: Float) {
                val harmonizedOffset = if (offset.isNaN()) 0f else offset
                slide(bottomSheet, harmonizedOffset)
            }
            override fun onStateChanged(bottomSheet: View, state: Int) = when (state) {
                STATE_HIDDEN -> dismiss()
                STATE_HALF_EXPANDED -> show()
                else -> Unit
            }
        })
}
