package com.lounah.vkmc.feature.feature_albums.albums.ui.recycler

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BasePagedAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentViewHolder
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumViewHolder

internal class AlbumsAdapter(
    private val onAlbumClicked: (AlbumUi) -> Unit,
    private val onAlbumLongClicked: (AlbumUi) -> Unit,
    private val onDeleteClicked: (AlbumUi) -> Unit,
    onRepeatPagedLoading: () -> Unit
) : BasePagedAdapter(onRepeatPagedLoading) {

    override fun onViewAttachedToWindow(holder: BaseViewHolder2<ViewTyped>) {
        super.onViewAttachedToWindow(holder)
        runCatching {
            val holder = holder.asType<AlbumViewHolder>()
            val album = holder.itemView.tag.asType<AlbumUi>()
            if (album.isInEditMode && album.isEditable)
                holder.itemView.startAnimation(holder.shakingAnimations.random())
        }
    }

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped> {
        return when (viewType) {
            R.layout.item_album -> AlbumViewHolder(
                view,
                onAlbumClicked,
                onAlbumLongClicked,
                onDeleteClicked
            ).asType()
            else -> error("Unknown view type")
        }
    }

    override fun setItems(items: List<ViewTyped>) {
        val merged = items.associateBy(ViewTyped::uid).values.toList()
        val callback = AlbumsDiffUtilCallback(itemsInternal, merged)
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(merged)
        diff.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_album -> (holder as AlbumViewHolder).bind(item.asType())
            R.layout.item_empty_content -> (holder as EmptyContentViewHolder).bind(item.asType())
        }
    }
}

internal class AlbumsDiffUtilCallback(
    private val oldItems: List<ViewTyped>,
    private val newItems: List<ViewTyped>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].uid == newItems[newItemPosition].uid
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (oldItems[oldItemPosition] is AlbumUi && newItems[newItemPosition] is AlbumUi) {
            (oldItems[oldItemPosition] as AlbumUi).isInEditMode != (newItems[newItemPosition] as AlbumUi).isInEditMode || (
                    (oldItems[oldItemPosition] as AlbumUi).subtitle != (newItems[newItemPosition] as AlbumUi).subtitle
                    )
        } else null
    }
}
