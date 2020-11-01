package com.lounah.vkmc.feature_places.places.feed.details.domain

import android.location.Geocoder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lounah.vkmc.core.core_vk.domain.GetCityById
import com.lounah.vkmc.feature_places.places.map.domain.StoriesRepository
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi
import io.reactivex.Observable

class GetStoryById(
    private val repository: StoriesRepository,
    private val geocoder: Geocoder,
    private val getCityById: GetCityById
) : (String) -> Observable<StoryUi> {

    override fun invoke(storyId: String): Observable<StoryUi> {
        return repository.getStoryById(storyId)
            .map { story ->
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
