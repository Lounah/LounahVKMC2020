package com.lounah.vkmc.feature.feature_market.cities.di

import com.lounah.vkmc.core.core_vk.domain.GetCities
import com.lounah.vkmc.feature.feature_market.cities.domain.CitiesListMapper
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListPresenter
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListPresenterFactory

interface CitiesListComponent {
    val citiesListPresenterFactory: CitiesListPresenterFactory
}

fun CitiesListComponent(): CitiesListComponent = object : CitiesListComponent {

    override val citiesListPresenterFactory: CitiesListPresenterFactory =
        CitiesListPresenterFactory(GetCities(), CitiesListMapper())
}
