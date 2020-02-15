package com.lounah.vkmc.feature.feature_image_picker.presentation

import android.content.Context
import android.os.Environment.DIRECTORY_PICTURES
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lounah.vkmc.core.extensions.cameraIntent
import java.io.File

internal class CameraStarter(
    private val authority: String = "com.lounah.vkmc"
) : (AppCompatActivity, Int) -> Unit {

    var currentPhoto: String? = null

    override fun invoke(activity: AppCompatActivity, requestCode: Int) {
        val photoUri =  FileProvider.getUriForFile(
            activity,
            authority,
            createImage(activity)
        )
        activity.cameraIntent(requestCode, photoUri)
    }

    private fun createImage(context: Context): File {
        val timestamp = System.currentTimeMillis()
        val format = ".jpg"
        val dir = context.getExternalFilesDir(DIRECTORY_PICTURES)

        return File.createTempFile("$timestamp", format, dir).apply {
            currentPhoto = absolutePath
        }
    }
}
