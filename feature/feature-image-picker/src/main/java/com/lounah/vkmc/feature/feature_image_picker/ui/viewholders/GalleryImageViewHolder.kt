package com.lounah.vkmc.feature.feature_image_picker.ui.viewholders

import android.view.View
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_image_picker.R
import kotlinx.android.synthetic.main.item_image_picker_photo.*

internal data class GalleryImageUi(
    override val uid: String,
    override val viewType: Int = R.layout.item_image_picker_photo
) : ViewTyped

internal class GalleryImageViewHolder(view: View, clicks: RecyclerItemClicksObservable) :
    BaseViewHolder<GalleryImageUi>(view) {

    init {
        clicks.accept(this) {
//            root.isSelected = !root.isSelected
//            checkbox.isChecked = !checkbox.isChecked
        }
    }

    override fun bind(item: GalleryImageUi) {
        super.bind(item)
        image.load(item.uid)
    }
}
