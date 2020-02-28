package com.lounah.vkmc.feature.feature_albums.createalbum.di

import com.lounah.vkmc.core.core_vk.domain.CreateAlbum
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumPresenter

interface CreateAlbumModule {
    val createAlbumPresenter: CreateAlbumPresenter
}

fun CreateAlbumModule(): CreateAlbumModule = object : CreateAlbumModule {

    override val createAlbumPresenter: CreateAlbumPresenter = CreateAlbumPresenter(CreateAlbum())
}
