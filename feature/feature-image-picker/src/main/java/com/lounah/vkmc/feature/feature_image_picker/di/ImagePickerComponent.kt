package com.lounah.vkmc.feature.feature_image_picker.di

import android.app.Activity
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerPresenter
import com.lounah.vkmc.feature.feature_image_picker.ui.util.Value
import java.io.File

interface ImagePickerComponent {
    val presenter: ImagePickerPresenter
    val cameraStarter: (Activity, Int) -> Unit
    val createdPhotoValue: Value<File>
}

fun ImagePickerComponent(
    deps: ImagePickerDependencies
): ImagePickerComponent =
    object : ImagePickerComponent, ImagePickerModule by ImagePickerModule(deps),
        ImagePickerDependencies by deps {}