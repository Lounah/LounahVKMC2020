package com.lounah.vkmc.feature.feature_sharing.di

import android.content.Context

interface SharingModule {
    val appContext: Context
}

fun SharingModule(
    deps: SharingDependencies
): SharingModule = object : SharingModule {
    override val appContext: Context = deps.appContext
}
