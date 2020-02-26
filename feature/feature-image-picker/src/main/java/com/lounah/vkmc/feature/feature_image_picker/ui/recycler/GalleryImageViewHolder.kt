package com.lounah.vkmc.feature.feature_image_picker.ui.recycler

import android.graphics.Bitmap.CompressFormat.JPEG
import android.view.View
import com.bumptech.glide.Priority.IMMEDIATE
import com.bumptech.glide.load.engine.DiskCacheStrategy.AUTOMATIC
import com.bumptech.glide.request.RequestOptions
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
        val requestOptions = RequestOptions()
            .encodeQuality(75)
            .override(itemView.measuredWidth, itemView.measuredWidth)
            .priority(IMMEDIATE)
            .placeholder(R.drawable.bg_image_loader_placeholder)
            .diskCacheStrategy(AUTOMATIC)
            .encodeFormat(JPEG)
        image.load(item.uid, requestOptions)
    }
}
