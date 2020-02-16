package com.lounah.vkmc.feature.feature_sharing.di

import android.content.Context

interface SharingComponent {
    val appContext: Context
}

fun SharingComponent(
    deps: SharingDependencies
): SharingComponent = object : SharingComponent, SharingModule by SharingModule(deps) {}
