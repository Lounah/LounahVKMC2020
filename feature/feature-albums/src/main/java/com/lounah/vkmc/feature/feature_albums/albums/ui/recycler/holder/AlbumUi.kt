package com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_albums.R

data class AlbumUi(
    override val uid: String,
    val thumb: String,
    val title: String,
    val subtitle: String,
    val size: Int,
    val isEditable: Boolean = true,
    val isInEditMode: Boolean = false,
    override val viewType: Int = R.layout.item_album
) : ViewTyped {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlbumUi

        if (uid != other.uid) return false
        if (title != other.title) return false
        if (isEditable != other.isEditable) return false
        if (isInEditMode != other.isInEditMode) return false
        if (viewType != other.viewType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isEditable.hashCode()
        result = 31 * result + isInEditMode.hashCode()
        result = 31 * result + viewType
        return result
    }
}
