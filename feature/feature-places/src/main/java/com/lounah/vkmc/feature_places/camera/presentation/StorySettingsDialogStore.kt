package com.lounah.vkmc.feature_places.camera.presentation

import com.lounah.vkmc.core.arch.RxStore
import com.lounah.vkmc.core.arch.impl.rxStore

class StorySettingsDialogStore(
    publishStory: PublishStoryCommandHandler
) : RxStore<StorySettingsState, StorySettingsEvent, StorySettingsNews> by rxStore(
    initialState = StorySettingsState(),
    commandsHandlers = listOf(publishStory),
    update = StorySettingsUpdater()
)
