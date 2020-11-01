package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.core.di.ComponentStorage

internal object InjectorInitializer {

    fun initialize(context: Context) {
        val appComponent = AppComponent(context)
        listOf(
            appComponent,
            appComponent.challengeFeatureComponent(),
            appComponent.imagePickerComponent(),
            appComponent.userGroupsComponent(),
            appComponent.groupDetailsComponent(),
            appComponent.marketsComponent(),
            appComponent.goodsComponent(),
            appComponent.citiesListComponent(),
            appComponent.productDetailsComponent(),
            appComponent.albumsComponent(),
            appComponent.eventsMapComponent(),
            appComponent.storiesPlacesComponent()
        ).also(ComponentStorage::addComponents)
    }
}
