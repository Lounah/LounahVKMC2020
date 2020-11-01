package com.lounah.vkmc.feature_places.camera.presentation

import com.lounah.vkmc.core.arch.Next
import com.lounah.vkmc.core.arch.Update
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsCommand.PublishStory
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsEvent.*
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsNews.CloseDialog
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsNews.ShowError

class StorySettingsUpdater : Update<StorySettingsState, StorySettingsEvent, StorySettingsCommand> {

    override fun update(
        state: StorySettingsState,
        event: StorySettingsEvent
    ): Next<StorySettingsState, StorySettingsCommand> {
        return when (event) {
            is OnTitleChanged -> Next(state.copy(title = event.title))
            is OnCommentChanged -> Next(state.copy(comment = event.comment))
            is OnStoryLoadingFailed -> Next(state.copy(isLoading = false), commands = listOf(ShowError))
            is OnLocationObtained -> Next(state.copy(lat = event.lat, long = event.long))
            is OnPublishClicked -> {
                val command = PublishStory(
                    title = state.title,
                    comment = state.comment,
                    lat = state.lat,
                    lng = state.long,
                    story = event.story
                )
                return Next(state.copy(isLoading = true), commands = listOf(command))
            }
            is OnStoryUploaded -> Next(state.copy(isLoading = false), commands = listOf(CloseDialog))
        }
    }
}
