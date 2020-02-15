package com.lounah.vkmc.feature.challenge_feature.di

import android.content.Context

interface ChallengeFeatureComponent {
    val appContext: Context
}

fun ChallengeFeatureComponent(
    deps: ChallengeFeatureDependencies
): ChallengeFeatureComponent = object : ChallengeFeatureComponent,
        ChallengeFeatureModule by ChallengeFeatureModule(deps) {}