package com.lounah.vkmc.feature.feature_image_picker.di

import android.app.Activity
import com.lounah.vkmc.feature.feature_image_picker.domain.CameraStarter
import com.lounah.vkmc.feature.feature_image_picker.domain.GetGalleryPhotos
import com.lounah.vkmc.feature.feature_image_picker.domain.PhotoFileCreator
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerPresenter
import com.lounah.vkmc.feature.feature_image_picker.ui.util.MemoryValueImpl
import com.lounah.vkmc.feature.feature_image_picker.ui.util.Value
import io.reactivex.Single
import java.io.File

interface ImagePickerModule {
    val presenter: ImagePickerPresenter
    val cameraStarter: (Activity, Int) -> Unit
    val createdPhotoValue: Value<File>
}

fun ImagePickerModule(deps: ImagePickerDependencies): ImagePickerModule =
    object : ImagePickerModule {

        override val createdPhotoValue =
            MemoryValueImpl<File>()
        private val authority = "com.lounah.vkmc"

        private val photoFileCreator: () -> File = PhotoFileCreator(deps.appContext, createdPhotoValue)

        private val getGalleryPhotos: () -> Single<List<String>>
            get() = GetGalleryPhotos(deps.appContext)

        override val cameraStarter: (Activity, Int) -> Unit
            get() = CameraStarter(photoFileCreator, authority)

        override val presenter: ImagePickerPresenter
            get() = ImagePickerPresenter(getGalleryPhotos, createdPhotoValue)
    }