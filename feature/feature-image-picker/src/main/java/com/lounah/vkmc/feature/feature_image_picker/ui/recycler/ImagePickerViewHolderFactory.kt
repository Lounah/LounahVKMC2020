package com.lounah.vkmc.feature.feature_image_picker.ui.recycler

import android.view.View
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.HolderFactory
import com.lounah.vkmc.feature.feature_image_picker.R

internal class ImagePickerViewHolderFactory : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_image_picker_pick_from_camera -> CameraPickerViewHolder(view, clicks)
            R.layout.item_image_picker_photo -> GalleryImageViewHolder(view, clicks)
            else -> null
        }
    }
}
