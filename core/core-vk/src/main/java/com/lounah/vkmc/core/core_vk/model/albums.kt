package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep

@Keep
data class AlbumsResponse(
    val response: Albums
)

@Keep
data class Albums(val items: List<Album>)

@Keep
data class Album(
    val id: String,
    val title: String,
    val size: Int,
    val sizes: List<AlbumPhotoSize>
) {
    val photo: String
        get() = sizes.maxBy { it.width * it.height }?.src.orEmpty()
}

@Keep
data class AlbumPhotoSize(
    val src: String,
    val width: Int,
    val height: Int
)
