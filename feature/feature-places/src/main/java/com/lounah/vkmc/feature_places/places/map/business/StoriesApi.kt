package com.lounah.vkmc.feature_places.places.map.business

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import com.lounah.vkmc.feature_places.places.map.domain.GetNearestCity
import com.lounah.vkmc.feature_places.places.model.Story
import io.reactivex.Observable

internal interface StoriesApi {
    fun getAllStories(): Observable<List<Story>>
    fun getStoryById(id: String): Observable<Story>
    fun getNearestStories(location: LatLng): Observable<List<Story>>
}

internal class StoriesApiImpl(
    private val storageReference: StorageReference,
    private val storiesCollection: CollectionReference,
    private val getNearestCity: GetNearestCity,
    private val storiesMetadataParser: (DocumentSnapshot) -> Story
) : StoriesApi {

    override fun getAllStories(): Observable<List<Story>> {
        return Observable.create { emitter ->
            storiesCollection.get().addOnSuccessListener { stories ->
                stories.documents.map(storiesMetadataParser).also(emitter::onNext)
            }.addOnFailureListener(emitter::onError)
        }
    }

    override fun getNearestStories(location: LatLng): Observable<List<Story>> {
        val nearestCity = getNearestCity(location)
        return getAllStories().map { it.filter { it.cityId == nearestCity }  }
    }

    override fun getStoryById(id: String): Observable<Story> {
        return Observable.create { emitter ->
            storiesCollection.document(id).get()
                .addOnSuccessListener { doc -> emitter.onNext(storiesMetadataParser(doc)) }
                .addOnFailureListener(emitter::onError)
        }
    }
}
