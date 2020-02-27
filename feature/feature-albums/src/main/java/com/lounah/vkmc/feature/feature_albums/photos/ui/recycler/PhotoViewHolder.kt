package com.lounah.vkmc.feature.feature_albums.photos.ui.recycler

import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_albums.R
import kotlinx.android.synthetic.main.item_album_photo.*

data class PhotoUi(
    override val uid: String,
    val path: String,
    override val viewType: Int = R.layout.item_album_photo
) : ViewTyped

internal class PhotoViewHolder(
    view: View,
    onPhotoClicked: (PhotoUi) -> Unit
) : BaseViewHolder2<PhotoUi>(view, onPhotoClicked) {

    override fun bind(item: PhotoUi) {
        super.bind(item)
        albumsImage.load(
            item.path,
            RequestOptions().placeholder(R.drawable.bg_image_loader_placeholder)
        )
    }
}