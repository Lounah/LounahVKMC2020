package com.lounah.vkmc.feature_places.places.preview.domain

import android.location.Geocoder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lounah.vkmc.core.core_vk.domain.GetCityById
import com.lounah.vkmc.feature_places.places.model.Story
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi

internal class StoryPreviewMapper(
    private val getCityById: GetCityById,
    private val geocoder: Geocoder
) : (List<Story>) -> List<StoryUi> {

    override fun invoke(stories: List<Story>): List<StoryUi> {
        return stories.map { story ->
            val addresses =
                geocoder.getFromLocation(story.latitude.toDouble(), story.longitude.toDouble(), 1)
            val address = addresses[0].thoroughfare
            StoryUi(
                id = story.id,
                title = story.title,
                cityId = story.cityId,
                author = story.author,
                comment = story.comment,
                cityName = address,
                latitude = story.latitude,
                longitude = story.longitude,
                thumbnailUri = Firebase.storage.getReferenceFromUrl("gs://vkmc2020.appspot.com/${story.thumbnailUri}"),
                uri = story.uri,
                views = story.views
            )
        }
    }
}
