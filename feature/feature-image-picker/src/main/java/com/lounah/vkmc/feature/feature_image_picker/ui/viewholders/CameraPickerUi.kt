package com.lounah.vkmc.feature.feature_image_picker.ui.viewholders

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_image_picker.R

internal object CameraPickerUi : ViewTyped {
    override val uid: String = "CameraPickerUi"
    override val viewType: Int = R.layout.item_image_picker_pick_from_camera
}
