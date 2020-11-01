package com.lounah.vkmc.feature_places.places.map.domain

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.model.Story

internal object StoriesMapper : (List<Story>) -> List<StoriesClusterItem> {

    override fun invoke(stories: List<Story>) =
        stories.map { story ->
            StoriesClusterItem(
                id = story.id,
                photoRef = Firebase.storage.getReferenceFromUrl("gs://vkmc2020.appspot.com/${story.thumbnailUri}"),
                cityId = story.cityId,
                latLng = LatLng(story.latitude.toDouble(), story.longitude.toDouble())
            )
        }
}
