package com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.extensions.animateScale
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_albums.R
import kotlinx.android.synthetic.main.item_album.*

internal class AlbumViewHolder(
    view: View,
    onAlbumClick: (AlbumUi) -> Unit,
    onAlbumLongClick: (AlbumUi) -> Unit,
    onDeleteClick: (AlbumUi) -> Unit
) : BaseViewHolder2<AlbumUi>(view, onAlbumClick, onAlbumLongClick) {

    val shakingAnimations = listOf(
        AnimationUtils.loadAnimation(itemView.context, R.anim.shaking_ltr),
        AnimationUtils.loadAnimation(itemView.context, R.anim.shaking_rtl)
    )

    init {
        delete.setOnClickListener { onDeleteClick(itemView.tag.asType()) }
        itemView.setOnClickListener {
            val album = itemView.tag.asType<AlbumUi>()
            if (!album.isInEditMode) onAlbumClick(itemView.tag.asType())
        }
        itemView.setOnLongClickListener {
            val album = itemView.tag.asType<AlbumUi>()
            if (!album.isInEditMode) onAlbumLongClick(itemView.tag.asType())
            true
        }
    }

    override fun bind(item: AlbumUi) {
        super.bind(item)
        title.text = item.title
        subtitle.text = item.subtitle
        image.load(item.thumb, RequestOptions().placeholder(R.drawable.bg_album_placeholder))
        when {
            item.isInEditMode && item.isEditable -> {
                delete.animateScale(1)
                root.startAnimation(shakingAnimations.random())
            }
            !item.isEditable && item.isInEditMode -> {
                root.foreground = drawable(R.drawable.bg_album_dim)
            }
            else -> {
                delete.animateScale(0)
                root.clearAnimation()
                root.foreground = null
            }
        }
    }

    override fun bind(item: AlbumUi, payloads: List<Any>) {
        super.bind(item, payloads)
        subtitle.text = item.subtitle
        image.load(item.thumb, RequestOptions().placeholder(R.drawable.bg_album_placeholder))
        when {
            item.isInEditMode && item.isEditable -> {
                delete.animateScale(1)
                root.startAnimation(shakingAnimations.random())
            }
            !item.isEditable && item.isInEditMode -> {
                root.foreground = drawable(R.drawable.bg_album_dim)
            }
            else -> {
                delete.animateScale(0)
                root.clearAnimation()
                root.foreground = null
            }
        }
    }

    private fun drawable(res: Int): Drawable = ContextCompat.getDrawable(itemView.context, res)!!
}
