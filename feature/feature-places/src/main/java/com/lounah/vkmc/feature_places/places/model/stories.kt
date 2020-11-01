package com.lounah.vkmc.feature_places.places.model

import androidx.annotation.Keep
import com.google.firebase.storage.StorageMetadata
import com.lounah.vkmc.feature_places.places.map.domain.CityId

@Keep
data class Story(
    val id: String,
    val cityId: CityId,
    val title: String,
    val latitude: String,
    val longitude: String,
    val uri: String,
    val thumbnailUri: String,
    val views: Int,
    val comment: String,
    val author: String
)

@Keep
internal fun StorageMetadata.toStory(): Story {
    return Story(
        getCustomMetadata("id").orEmpty(),
        getCustomMetadata("cityId").orEmpty(),
        getCustomMetadata("title").orEmpty(),
        getCustomMetadata("latitude").orEmpty(),
        getCustomMetadata("longitude").orEmpty(),
        getCustomMetadata("uri").orEmpty(),
        getCustomMetadata("thumbnailUri").orEmpty(),
        getCustomMetadata("views")?.toInt() ?: 0,
        getCustomMetadata("comment").orEmpty(),
        getCustomMetadata("author").orEmpty()
    )
}

