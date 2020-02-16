package com.lounah.vkmc.feature.feature_sharing

import android.os.Bundle
import android.view.View
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.core.ui.bottomsheet.attachStickyFooter
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import kotlinx.android.synthetic.main.fragment_create_wall_post.*

internal class CreateWallPostBottomSheetDialog : BaseBottomSheetDialogFragment(R.layout.fragment_create_wall_post) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.attachStickyFooter(buttonContainer)
        val selectedPicPath = arguments!!.getString(ARG_SELECTED_PIC).orEmpty()
        selectedImage.load(selectedPicPath)
        selectedImage.setOnClickListener {
            ImageViewerActivity.start(requireContext(), arrayListOf(selectedPicPath))
        }
    }

    companion object {
        private const val ARG_SELECTED_PIC = "ARG_SELECTED_PIC"

        fun newInstance(selectedPicPath: String): CreateWallPostBottomSheetDialog {
            return CreateWallPostBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_SELECTED_PIC, selectedPicPath)
                }
            }
        }
    }
}
