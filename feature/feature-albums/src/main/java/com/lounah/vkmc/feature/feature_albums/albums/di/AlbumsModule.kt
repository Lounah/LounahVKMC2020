package com.lounah.vkmc.feature.feature_albums.albums.di

import com.lounah.vkmc.core.core_vk.domain.GetAlbums
import com.lounah.vkmc.feature.feature_albums.albums.domain.AlbumsMapper
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsPresenter
import com.lounah.vkmc.feature.feature_albums.di.AlbumsDependencies

interface AlbumsModule {
    val albumsPresenter: AlbumsPresenter
}

fun AlbumsModule(
    deps: AlbumsDependencies
): AlbumsModule = object : AlbumsModule {

    override val albumsPresenter: AlbumsPresenter =
        AlbumsPresenter(GetAlbums(), AlbumsMapper(deps.appContext))
}