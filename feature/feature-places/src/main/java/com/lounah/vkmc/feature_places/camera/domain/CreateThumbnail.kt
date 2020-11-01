package com.lounah.vkmc.feature_places.camera.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.net.Uri
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.storage.StorageReference
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import io.reactivex.Observable
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CreateThumbnail(
    private val context: Context,
    private val storageRef: StorageReference
) : (File) -> Observable<String> {

    override fun invoke(from: File): Observable<String> {
        return Observable.create { emitter ->
            from.extractFileThumbnail { thumb ->
                storageRef
                    .child(thumb.name)
                    .putFile(Uri.fromFile(thumb))
                    .addOnSuccessListener { emitter.onNext(thumb.name) }
                    .addOnFailureListener(emitter::onError)
            }
        }
    }

    private fun File.extractFileThumbnail(onLoaded: (File) -> Unit) {
        val tempFile = File.createTempFile(UUID.randomUUID().toString(), "jpg")
        GlideApp
            .with(context)
            .asBitmap()
            .load(Uri.fromFile(this))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val os = BufferedOutputStream(FileOutputStream(tempFile))
                    resource.compress(JPEG, 100, os);
                    os.close()
                    onLoaded(tempFile)
                }
            })
    }
}
