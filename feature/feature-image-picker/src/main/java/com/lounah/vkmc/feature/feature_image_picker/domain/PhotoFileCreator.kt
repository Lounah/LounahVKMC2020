package com.lounah.vkmc.feature.feature_image_picker.domain

import android.content.Context
import android.os.Environment
import com.lounah.vkmc.feature.feature_image_picker.ui.util.Value
import java.io.File

internal class PhotoFileCreator(
    private val context: Context,
    private val createdPhoto: Value<File>
) : () -> File {

    override fun invoke(): File {
        val timestamp = System.currentTimeMillis()
        val format = ".jpg"
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("$timestamp", format, dir).also(createdPhoto::set)
    }
}
