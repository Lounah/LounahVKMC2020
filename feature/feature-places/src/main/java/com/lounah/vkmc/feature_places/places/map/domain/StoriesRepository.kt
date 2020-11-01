package com.lounah.vkmc.feature_places.places.map.domain

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.feature_places.places.map.business.StoriesApi
import com.lounah.vkmc.feature_places.places.model.Story
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

interface StoriesRepository {
    fun putStory(file: File, location: LatLng): Completable
    fun getStoriesByIds(ids: List<String>): Observable<List<Story>>
    fun getNearestStories(location: LatLng): Observable<List<Story>>
    fun getStoryById(id: String): Observable<Story>
}

internal class StoriesRepositoryImpl(
    private val storiesApi: StoriesApi
) : StoriesRepository {

    private val inMemoryStories = CopyOnWriteArrayList<Story>()

    override fun putStory(file: File, location: LatLng): Completable {
        val id = UUID.randomUUID().timestamp().toString()
        return storiesApi.saveStory(id, "", "",file, location)
            .map(inMemoryStories::add)
            .ignoreElement()
    }

    override fun getNearestStories(location: LatLng): Observable<List<Story>> {
        return storiesApi.getNearestStories(location)
            .doOnNext(::insertUniqueStories)
    }

    override fun getStoriesByIds(ids: List<String>): Observable<List<Story>> {
        return storiesApi.getAllStories()
            .map { it.filter { ids.contains(it.id) } }
            .doOnNext(::insertUniqueStories)
    }

    override fun getStoryById(id: String): Observable<Story> {
        return storiesApi.getStoryById(id)
            .doOnNext { insertUniqueStories(listOf(it)) }
    }

    private fun insertUniqueStories(stories: List<Story>) {
        inMemoryStories.addAll(stories.filterNot(inMemoryStories::contains))
    }
}
