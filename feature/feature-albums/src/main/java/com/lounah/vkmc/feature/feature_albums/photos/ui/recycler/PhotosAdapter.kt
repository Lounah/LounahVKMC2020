package com.lounah.vkmc.feature.feature_albums.photos.ui.recycler

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BasePagedAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentViewHolder
import com.lounah.vkmc.core.recycler.paging.core.ViewTypedDiffCallback
import com.lounah.vkmc.feature.feature_albums.R

internal class PhotosAdapter(
    private val onPhotoClicked: (PhotoUi) -> Unit,
    private val onPhotoLongClicked: (PhotoUi) -> Unit,
    onRepeatPagedLoading: () -> Unit
) : BasePagedAdapter(onRepeatPagedLoading) {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped> {
        return when (viewType) {
            R.layout.item_album_photo -> PhotoViewHolder(view, onPhotoClicked, onPhotoLongClicked).asType()
            R.layout.item_album_header -> AlbumHeaderViewHolder(view).asType()
            else -> error("Unknown view type")
        }
    }

    override fun setItems(items: List<ViewTyped>) {
        val unique = items.distinct()
        val callback = ViewTypedDiffCallback(itemsInternal, unique)
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(unique)
        diff.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_album_photo -> (holder as PhotoViewHolder).bind(item.asType())
            R.layout.item_album_header -> (holder as AlbumHeaderViewHolder).bind(item.asType())
            R.layout.item_empty_content -> (holder as EmptyContentViewHolder).bind(item.asType())
        }
    }
}
