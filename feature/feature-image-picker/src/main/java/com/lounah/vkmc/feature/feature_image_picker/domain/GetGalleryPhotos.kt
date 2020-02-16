package com.lounah.vkmc.feature.feature_image_picker.domain

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import io.reactivex.Single

internal class GetGalleryPhotos(
    private val context: Context
) : () -> Single<List<String>> {

    override fun invoke(): Single<List<String>> {
        return Single.create { emitter ->
            val uri = EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns.DATA, BUCKET_DISPLAY_NAME)
            val result = mutableListOf<String>()

            context.contentResolver.query(uri, projection, null, null, null)?.run {
                val index = runCatching {
                    getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                }.getOrElse(emitter::onError)

                while (moveToNext()) {
                    result.add(getString(index as Int))
                }

                close()
                emitter.onSuccess(result)
            } ?: emitter.onError(Throwable())
        }
    }
}