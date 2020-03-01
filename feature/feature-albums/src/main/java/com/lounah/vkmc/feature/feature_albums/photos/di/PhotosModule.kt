package com.lounah.vkmc.feature.feature_albums.photos.di

import com.lounah.vkmc.core.core_vk.domain.DeletePhoto
import com.lounah.vkmc.core.core_vk.domain.GetPhotos
import com.lounah.vkmc.core.core_vk.domain.UploadPhoto
import com.lounah.vkmc.feature.feature_albums.photos.domain.PhotosMapper
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosPresenterFactory

interface PhotosModule {
    val photosPresenterFactory: PhotosPresenterFactory
}

fun PhotosModule(): PhotosModule = object : PhotosModule {
    override val photosPresenterFactory: PhotosPresenterFactory =
        PhotosPresenterFactory(GetPhotos(), PhotosMapper(), UploadPhoto(), DeletePhoto())
}
