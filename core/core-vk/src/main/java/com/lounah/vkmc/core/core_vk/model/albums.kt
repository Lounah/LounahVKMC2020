package com.lounah.vkmc.core.core_vk.model


data class AlbumsResponse(
    val response: Albums
)

data class Albums(val items: List<Album>)

data class Album(
    val id: String,
    val title: String,
    val size: Int,
    val sizes: List<AlbumPhotoSize>
) {
    val photo: String
        get() = sizes.maxBy { it.width * it.height }?.src.orEmpty()
}

data class AlbumPhotoSize(
    val src: String,
    val width: Int,
    val height: Int
)
