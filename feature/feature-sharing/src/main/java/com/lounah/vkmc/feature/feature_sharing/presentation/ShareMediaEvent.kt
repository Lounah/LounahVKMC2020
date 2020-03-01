package com.lounah.vkmc.feature.feature_sharing.presentation

import com.lounah.vkmc.core.core_vk.domain.wall.WallPost

sealed class ShareMediaEvent {
    class OnPostSuccessfullyShared(val post: WallPost) : ShareMediaEvent()
    object ShowUploadingError : ShareMediaEvent()
}
