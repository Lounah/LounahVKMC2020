package com.lounah.vkmc.feature_places.places.preview.presentation

import com.lounah.vkmc.feature_places.places.preview.domain.GetStoryPreviews
import com.lounah.vkmc.feature_places.places.preview.domain.StoriesIds
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class StoriesFeedPresenter(
    private val getStoryPreviews: GetStoryPreviews,
    private val incrementStoryViews: (storyId: String) -> Completable,
    private val getStoryById: (storyId: String) -> Observable<StoryUi>
) {

    fun getPreviews(ids: StoriesIds): Observable<List<StoryUi>> {
        return Observable.defer {
            getStoryPreviews(ids)
                .subscribeOn(io())
                .observeOn(mainThread())
        }
    }

    fun getStory(id: String): Observable<StoryUi> {
        return getStoryById(id)
            .subscribeOn(io())
            .observeOn(mainThread())
    }

    fun onStoryIncrementViews(storyId: String): Completable {
        return incrementStoryViews(storyId)
            .subscribeOn(io())
            .observeOn(mainThread())
    }
}
