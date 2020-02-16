package com.lounah.vkmc.feature.feature_sharing.di

import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaPresenter

interface SharingComponent {
    val createWallPostFragmentPresenter: ShareMediaPresenter
}

fun SharingComponent(
    deps: SharingDependencies
): SharingComponent =
    object : SharingComponent, SharingModule by SharingModule(), SharingDependencies by deps {}
