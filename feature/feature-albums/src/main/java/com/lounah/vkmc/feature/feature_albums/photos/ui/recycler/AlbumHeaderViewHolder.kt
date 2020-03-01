package com.lounah.vkmc.feature.feature_albums.photos.ui.recycler

import android.view.View
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.feature.feature_albums.R
import kotlinx.android.synthetic.main.item_album_header.*

data class AlbumHeaderUi(
    val text: String,
    override val uid: String = "AlbumHeader",
    override val viewType: Int = R.layout.item_album_header
) : ViewTyped

internal class AlbumHeaderViewHolder(view: View) : BaseViewHolder2<AlbumHeaderUi>(view) {

    override fun bind(item: AlbumHeaderUi) {
        super.bind(item)
        header.text = item.text
    }
}