package com.lounah.vkmc.feature_places.places.map.config.markers

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.storage.StorageReference
import com.google.maps.android.clustering.ClusterItem

data class StoriesClusterItem(
    val id: String,
    val photoRef: StorageReference,
    val cityId: String,
    val latLng: LatLng
) : ClusterItem {

    override fun getPosition(): LatLng = latLng

    override fun getTitle() = ""

    override fun getSnippet() = ""
}
