package com.lounah.vkmc.feature.feature_image_picker.ui

import com.lounah.vkmc.feature.feature_image_picker.presentation.CAMERA_REQ_CODE
import com.lounah.vkmc.feature.feature_image_picker.presentation.GALLERY_REQ_CODE

enum class PickerType(val permissionRequestCode: Int) {
    GALLERY(GALLERY_REQ_CODE), CAMERA(CAMERA_REQ_CODE)
}
