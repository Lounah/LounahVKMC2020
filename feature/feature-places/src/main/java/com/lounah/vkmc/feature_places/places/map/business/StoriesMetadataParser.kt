package com.lounah.vkmc.feature_places.places.map.business

import com.google.firebase.firestore.DocumentSnapshot
import com.lounah.vkmc.feature_places.places.model.Story

internal class StoriesMetadataParser : (DocumentSnapshot) -> Story {

    override fun invoke(doc: DocumentSnapshot): Story {
        return Story(
            id = doc.id,
            cityId = doc.get("cityId").toString(),
            title = doc.get("title").toString(),
            latitude = doc.get("latitude").toString(),
            longitude = doc.get("longitude").toString(),
            uri = doc.get("uri").toString(),
            thumbnailUri = doc.get("thumbnailUri").toString(),
            views = doc.get("views").toString().toInt(),
            comment = doc.get("comment").toString(),
            author = doc.get("author").toString()
        )
    }
}
