package com.lounah.vkmc.feature_places.places.preview.ui.recycler

import com.google.firebase.storage.StorageReference
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.places.map.domain.CityId

data class StoryUi(
    val id: String,
    val title: String,
    val cityId: CityId,
    val author: String,
    val cityName: String,
    val comment: String,
    val latitude: String,
    val longitude: String,
    val thumbnailUri: StorageReference,
    val uri: String,
    val views: Int = 0,
    override val viewType: Int = R.layout.item_story_preview
) : ViewTyped

object StoryLoadingUi : ViewTyped {
    override val viewType: Int = R.layout.view_stories_preview_shimmer
}
