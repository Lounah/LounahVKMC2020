package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.core.di.ComponentStorage

internal object InjectorInitializer {

    fun initialize(context: Context) {
        val appComponent = AppComponent(context)
        val challengeFeatureComponent = appComponent.challengeFeatureComponent()

        ComponentStorage.addComponent(appComponent)
        ComponentStorage.addComponent(challengeFeatureComponent)
    }
}