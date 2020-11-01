package com.lounah.vkmc.feature_places.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsDialogStore
import com.lounah.vkmc.feature_places.places.map.presentation.ClipsPlacesStore
import com.lounah.vkmc.feature_places.places.preview.presentation.StoriesFeedPresenter

interface ClipsPlacesComponent {
    val store: ClipsPlacesStore
    val storySettingsStore: StorySettingsDialogStore
    val storiesDialogPresenter: StoriesFeedPresenter
    val storageReference: StorageReference
}

fun ClipsPlacesComponent(deps: ClipsPlacesDependencies): ClipsPlacesComponent =
    object : ClipsPlacesComponent, ClipsPlacesModule by ClipsPlacesModule(deps),
        ClipsPlacesDependencies by deps {

        override val storageReference: StorageReference =
            Firebase.storage("gs://vkmc2020.appspot.com/").reference
    }
