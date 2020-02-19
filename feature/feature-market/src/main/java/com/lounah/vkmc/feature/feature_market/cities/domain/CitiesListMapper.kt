package com.lounah.vkmc.feature.feature_market.cities.domain

import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CityUi

class CitiesListMapper : (List<City>) -> List<CityUi> {

    override fun invoke(cities: List<City>): List<CityUi> {
        return cities.map {
            CityUi(
                uid = it.id,
                name = it.title,
                isChecked = false
            )
        }
    }
}
