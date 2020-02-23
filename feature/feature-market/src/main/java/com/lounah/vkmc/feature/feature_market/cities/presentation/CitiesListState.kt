package com.lounah.vkmc.feature.feature_market.cities.presentation

import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListAction.*
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CityUi

data class CitiesListState(
    val selectedCity: CityId = "1",
    val cities: List<CityUi> = emptyList()
)

internal fun CitiesListState.reduce(action: CitiesListAction): CitiesListState {
    return when (action) {
        is InitialLoading -> copy(selectedCity = action.cityId)
        is OnCitySelected -> {
            val newCities = cities.map {
                if (it.uid == action.cityId)
                    it.copy(isChecked = true)
                else it.copy(isChecked = false)
            }
            copy(selectedCity = action.cityId, cities = newCities)
        }
        is OnCitiesLoaded -> {
            val newCities = action.cities.map {
                if (it.uid == selectedCity)
                    it.copy(isChecked = true)
                else it.copy(isChecked = false)
            }
            copy(cities = newCities)
        }
    }
}
