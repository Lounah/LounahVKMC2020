package com.lounah.vkmc.feature.challenge_feature.di

import android.content.Context

interface ChallengeFeatureModule {
    val appContext: Context
}

fun ChallengeFeatureModule(
    deps: ChallengeFeatureDependencies
) : ChallengeFeatureModule = object : ChallengeFeatureModule {
    override val appContext: Context = deps.appContext
}