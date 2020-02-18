package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.core.di.ComponentStorage

internal object InjectorInitializer {

    fun initialize(context: Context) {
        val appComponent = AppComponent(context)
        val challengeFeatureComponent = appComponent.challengeFeatureComponent()
        val imagePickerComponent = appComponent.imagePickerComponent()
        val userGroupsComponent = appComponent.userGroupsComponent()
        val groupDetailsComponent = appComponent.groupDetailsComponent()

        ComponentStorage.addComponent(appComponent)
        ComponentStorage.addComponent(challengeFeatureComponent)
        ComponentStorage.addComponent(imagePickerComponent)
        ComponentStorage.addComponent(groupDetailsComponent)
        ComponentStorage.addComponent(userGroupsComponent)
    }
}
