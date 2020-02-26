package com.lounah.vkmc.feature.feature_image_picker.ui.recycler

import android.view.View
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import com.lounah.vkmc.feature.feature_image_picker.R

internal object CameraPickerUi : ViewTyped {
    override val uid: String = "CameraPickerUi"
    override val viewType: Int = R.layout.item_image_picker_pick_from_camera
}

internal class CameraPickerViewHolder(
    view: View,
    clicks: RecyclerItemClicksObservable
) : BaseViewHolder<CameraPickerUi>(view) {

    init {
        clicks.accept(this)
    }
}