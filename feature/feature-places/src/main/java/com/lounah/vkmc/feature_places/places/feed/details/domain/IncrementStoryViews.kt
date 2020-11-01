package com.lounah.vkmc.feature_places.places.feed.details.domain

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lounah.vkmc.feature_places.places.map.domain.StoriesRepository
import io.reactivex.Completable

class IncrementStoryViews(
    private val repository: StoriesRepository
) : (String) -> Completable {

    override fun invoke(storyId: String): Completable {
        return repository.getStoryById(storyId)
            .flatMapCompletable { story ->
                Completable.create { emitter ->
                    Firebase.firestore.collection("stories").document(storyId)
                        .update("views", story.views + 1)
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener(emitter::onError)
                }
            }
    }
}
