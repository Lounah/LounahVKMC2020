package com.lounah.vkmc.feature.feature_map.map.ui.map

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

interface MarkerView {
    fun setIcon(path: String)

    fun setImage(image: Bitmap)

    fun setCounter(counter: Int)

    fun setBackground(@DrawableRes background: Int)
}
