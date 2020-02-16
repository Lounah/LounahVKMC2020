package com.lounah.vkmc.feature.feature_sharing.presentation

sealed class ShareMediaAction {
    object OnShareWallPostClicked : ShareMediaAction()
    object UploadError : ShareMediaAction()
    class OnTextInputChanged(val input: String) : ShareMediaAction()
    class OnImageSelected(val imagePath: String) : ShareMediaAction()
}
