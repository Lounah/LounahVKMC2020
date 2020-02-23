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
        val marketsComponent = appComponent.marketsComponent()
        val goodsComponent = appComponent.goodsComponent()
        val citiesListComponent = appComponent.citiesListComponent()
        val productDetailsComponent = appComponent.productDetailsComponent()

        ComponentStorage.addComponent(appComponent)
        ComponentStorage.addComponent(challengeFeatureComponent)
        ComponentStorage.addComponent(imagePickerComponent)
        ComponentStorage.addComponent(groupDetailsComponent)
        ComponentStorage.addComponent(citiesListComponent)
        ComponentStorage.addComponent(goodsComponent)
        ComponentStorage.addComponent(marketsComponent)
        ComponentStorage.addComponent(userGroupsComponent)
        ComponentStorage.addComponent(productDetailsComponent)
    }
}
