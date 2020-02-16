package com.lounah.vkmc.feature.feature_sharing.di

import android.net.Uri
import com.lounah.vkmc.core.core_vk.domain.CreateWallPost
import com.lounah.vkmc.core.core_vk.model.WallPost
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaPresenter
import io.reactivex.Single

interface SharingModule {
    val createWallPostFragmentPresenter: ShareMediaPresenter
}

fun SharingModule(): SharingModule = object : SharingModule {

    private val createWallPost: (String, List<Uri>) -> Single<WallPost> = CreateWallPost()

    override val createWallPostFragmentPresenter: ShareMediaPresenter =
        ShareMediaPresenter(createWallPost)
}
