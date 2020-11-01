package com.lounah.vkmc.feature_places.places.preview.domain

import com.lounah.vkmc.feature_places.places.map.domain.StoriesRepository
import com.lounah.vkmc.feature_places.places.model.Story
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi
import io.reactivex.Observable

typealias StoriesIds = List<String>

class GetStoryPreviews(
    private val repository: StoriesRepository,
    private val mapper: (List<Story>) -> List<StoryUi>
) : (StoriesIds) -> Observable<List<StoryUi>> {

    override fun invoke(ids: StoriesIds): Observable<List<StoryUi>> {
        return repository.getStoriesByIds(ids).map(mapper)
    }
}
