package com.lounah.vkmc.feature_places.camera.presentation

import java.io.File

data class StorySettingsState(
    val title: String = "",
    val comment: String = "",
    val lat: Double = 45.056039,
    val long: Double = 39.023480,
    val thumbnail: File? = null,
    val story: File? = null,
    val isLoading: Boolean = false
) {
    val isPublishEnabled: Boolean
        get() = title.isNotEmpty() && lat != 0.0 && long != 0.0
}

sealed class StorySettingsCommand {
    class PublishStory(
        val title: String,
        val comment: String,
        val lat: Double,
        val lng: Double,
        val story: File
    ) : StorySettingsCommand()
}

sealed class StorySettingsEvent {
    class OnTitleChanged(val title: String) : StorySettingsEvent()
    class OnCommentChanged(val comment: String) : StorySettingsEvent()
    class OnPublishClicked(val story: File) : StorySettingsEvent()
    object OnStoryLoadingFailed : StorySettingsEvent()
    class OnLocationObtained(val lat: Double, val long: Double) : StorySettingsEvent()
    object OnStoryUploaded : StorySettingsEvent()
}

sealed class StorySettingsNews : StorySettingsCommand() {
    object ShowError : StorySettingsNews()
    object CloseDialog : StorySettingsNews()
}
