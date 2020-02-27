package com.lounah.vkmc.feature.feature_albums.di

import com.lounah.vkmc.feature.feature_albums.albums.di.AlbumsModule
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsPresenter
import com.lounah.vkmc.feature.feature_albums.photos.di.PhotosModule
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosPresenterFactory

interface AlbumsComponent {
    val albumsPresenter: AlbumsPresenter
    val photosPresenterFactory: PhotosPresenterFactory
}

fun AlbumsComponent(deps: AlbumsDependencies): AlbumsComponent =
    object : AlbumsComponent,
        AlbumsModule by AlbumsModule(deps),
        PhotosModule by PhotosModule(),
        AlbumsDependencies by deps {}
