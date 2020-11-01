package com.lounah.vkmc.feature_places.camera.domain

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.lounah.vkmc.core.core_vk.business.commands.user.User
import com.lounah.vkmc.feature_places.places.map.domain.GetNearestCity
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.io.File

interface StoryCreator {
    fun create(
        title: String,
        comment: String,
        thumbnail: String,
        latLng: LatLng,
        story: File
    ): Observable<String>
}

class StoryCreatorImpl(
    private val getNearestCity: GetNearestCity,
    private val getUser: () -> Observable<User>,
    private val publishStoryVideo: (File) -> Observable<String>,
    private val collectionRef: CollectionReference
) : StoryCreator {
    override fun create(
        title: String,
        comment: String,
        thumbnail: String,
        latLng: LatLng,
        story: File
    ): Observable<String> {
        return Observables.zip(getUser(), publishStoryVideo(story)) { user, storyUri ->
            val author = "${user.firstName} ${user.lastName}"
            mapOf(
                "author" to author,
                "title" to title,
                "comment" to comment,
                "latitude" to latLng.latitude.toString(),
                "longitude" to latLng.longitude.toString(),
                "views" to "0",
                "cityId" to getNearestCity(latLng),
                "thumbnailUri" to thumbnail,
                "uri" to storyUri
            )
        }.flatMap { storyMap ->
            Observable.create<String> { emitter ->
                collectionRef.add(storyMap)
                    .addOnSuccessListener { emitter.onNext(it.id) }
                    .addOnFailureListener(emitter::onError)
            }
        }
    }
}
