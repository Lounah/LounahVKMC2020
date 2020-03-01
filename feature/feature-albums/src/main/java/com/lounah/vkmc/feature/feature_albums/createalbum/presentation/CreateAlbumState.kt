package com.lounah.vkmc.feature.feature_albums.createalbum.presentation

import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumAction.*

data class CreateAlbumState(
    val title: String = "",
    val description: String = "",
    val isPrivate: Boolean = false,
    val isLoading: Boolean = false
) {
    val canCreateAlbum: Boolean
        get() = title.isNotEmpty()
}

internal fun CreateAlbumState.reduce(action: CreateAlbumAction): CreateAlbumState {
    return when (action) {
        is OnTitleChanged -> copy(title = action.title)
        is OnDescriptionChanged -> copy(description = action.description)
        is OnPrivacyChanged -> copy(isPrivate = action.isPrivate)
        is OnCreateClicked -> copy(isLoading = true)
        is OnCreateFailed -> copy(isLoading = false)
    }
}
