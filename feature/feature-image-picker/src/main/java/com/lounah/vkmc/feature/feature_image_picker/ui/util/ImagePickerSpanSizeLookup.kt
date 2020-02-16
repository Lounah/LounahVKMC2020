package com.lounah.vkmc.feature.feature_image_picker.ui.util

import androidx.recyclerview.widget.GridLayoutManager

class ImagePickerSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int = if (position == 0) 3 else 1
}