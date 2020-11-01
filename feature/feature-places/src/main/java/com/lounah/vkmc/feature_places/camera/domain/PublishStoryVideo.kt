package com.lounah.vkmc.feature_places.camera.domain

import android.net.Uri
import com.google.firebase.storage.StorageReference
import io.reactivex.Observable
import java.io.File

class PublishStoryVideo(
    private val storageReference: StorageReference
) : (File) -> Observable<String> {

    override fun invoke(story: File): Observable<String> {
        return Observable.create { emitter ->
            storageReference
                .child(story.name)
                .putFile(Uri.fromFile(story))
                .addOnSuccessListener { emitter.onNext(story.name) }
                .addOnFailureListener(emitter::onError)
        }
    }
}
