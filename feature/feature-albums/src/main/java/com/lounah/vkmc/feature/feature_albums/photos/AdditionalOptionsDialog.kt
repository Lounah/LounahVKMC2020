package com.lounah.vkmc.feature.feature_albums.photos

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.feature.feature_albums.R
import kotlinx.android.synthetic.main.fragment_photo_additional_options.*
import kotlin.LazyThreadSafetyMode.NONE

internal class AdditionalOptionsDialog :
    BaseBottomSheetDialogFragment(R.layout.fragment_photo_additional_options) {

    private val photoId by lazy(NONE) {
        arguments!!.getString(ARG_PHOTO_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delete.setOnClickListener {
            targetFragment?.onActivityResult(
                RC_PHOTO_ACTION,
                RESULT_DELETE,
                Intent().putExtra(ARG_PHOTO_ID, photoId)
            )
            dismiss()
        }
        closeBtn.setOnClickListener { dismiss() }
    }

    companion object {
        const val RESULT_DELETE = 112
        const val RC_PHOTO_ACTION = 129
        const val ARG_PHOTO_ID = "arg_photo_id"

        fun newInstance(photoId: String): AdditionalOptionsDialog =
            AdditionalOptionsDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHOTO_ID, photoId)
                }
            }
    }
}
