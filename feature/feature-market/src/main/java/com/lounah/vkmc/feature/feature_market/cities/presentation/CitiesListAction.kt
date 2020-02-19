package com.lounah.vkmc.feature.feature_market.cities.presentation

import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CityUi

sealed class CitiesListAction {
    class InitialLoading(val cityId: CityId) : CitiesListAction()
    class OnCitySelected(val cityId: CityId) : CitiesListAction()
    class OnCitiesLoaded(val cities: List<CityUi>) : CitiesListAction()
}
