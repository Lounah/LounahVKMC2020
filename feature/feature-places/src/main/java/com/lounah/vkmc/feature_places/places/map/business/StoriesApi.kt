package com.lounah.vkmc.feature_places.places.map.business

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import com.lounah.vkmc.feature_places.places.map.domain.GetNearestCity
import com.lounah.vkmc.feature_places.places.model.Story
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

internal interface StoriesApi {
    fun saveStory(
        id: String,
        comment: String,
        title: String,
        story: File,
        location: LatLng
    ): Single<Story>

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

    override fun saveStory(
        id: String,
        comment: String,
        title: String,
        story: File,
        location: LatLng
    ): Single<Story> {
        val uri = Uri.fromFile(story)
//        val thumbnail = ThumbnailUtils.createVideoThumbnail(story.absolutePath, MINI_KIND)
//        val metadata = StorageMetadata.Builder()
//            .setCustomMetadata("id", id)
//            .setCustomMetadata("cityId", getNearestCity(location))
//            .setCustomMetadata("title", title)
//            .setCustomMetadata("latitude", location.latitude.toString())
//            .setCustomMetadata("longitude", location.longitude.toString())
//            .setCustomMetadata("uri", uri.toString())
//            .setCustomMetadata("views", "0")
//            .setCustomMetadata("comment", comment)
//            .setCustomMetadata("author", "Maksim Shchepalin")
//            .build()

        return Single.create { emitter ->
            storageReference.putFile(Uri.fromFile(story))
//                .addOnSuccessListener { emitter.onSuccess(metadata.toStory()) }
                .addOnFailureListener(emitter::onError)
        }
    }

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
