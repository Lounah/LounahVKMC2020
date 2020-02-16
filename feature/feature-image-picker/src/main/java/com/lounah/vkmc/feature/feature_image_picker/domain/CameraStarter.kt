package com.lounah.vkmc.feature.feature_image_picker.domain

import android.app.Activity
import androidx.core.content.FileProvider
import com.lounah.vkmc.core.extensions.cameraIntent
import java.io.File

internal class CameraStarter(
    private val photoFileCreator: () -> File,
    private val authority: String
) : (Activity, Int) -> Unit {

    override fun invoke(activity: Activity, requestCode: Int) {
        val photoUri = FileProvider.getUriForFile(
            activity,
            authority,
            photoFileCreator()
        )
        activity.cameraIntent(requestCode, photoUri)
    }
}
