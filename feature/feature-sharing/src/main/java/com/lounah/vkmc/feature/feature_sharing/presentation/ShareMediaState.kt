package com.lounah.vkmc.feature.feature_sharing.presentation

import android.net.Uri
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaAction.*
import java.io.File

data class ShareMediaState(
    val commentText: String = "",
    val selectedImage: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)

internal fun ShareMediaState.reduce(action: ShareMediaAction): ShareMediaState {
    return when (action) {
        is OnShareWallPostClicked -> copy(isLoading = true)
        is UploadError -> copy(isLoading = false)
        is OnTextInputChanged -> copy(commentText = action.input)
        is OnImageSelected -> copy(selectedImage = Uri.fromFile(File(action.imagePath)))
    }
}
