package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.domain.market.CityId
import io.reactivex.Single

private val inMemoryCities = listOf(
    City("1", "Москва", "Москве", 55.75 to 37.61),
    City("2", "Санкт-Петербург", "Санкт-Петербурге", 59.93 to 30.33),
    City("10", "Волгоград", "Волгограде", 48.70 to 44.51),
    City("37", "Владивосток", "Владивостоке", 43.11 to 131.88),
    City("153", "Хабаровск", "Хабаровске", 48.51 to 135.10),
    City("49", "Екатеринбург", "Екатеринбурге", 56.83 to 60.60),
    City("60", "Казань", "Казани", 55.83 to 49.06),
    City("61", "Калининград", "Калининграде", 54.71 to 20.45),
    City("72", "Краснодар", "Краснодаре", 45.03 to 38.98),
    City("73", "Красноярск", "Красноярске", 56.01 to 92.89),
    City("95", "Нижний новгород", "Нижнем новгороде", 56.29 to 43.93),
    City("99", "Новосибирск", "Новосибирске", 55.00 to 82.93),
    City("104", "Омск", "Омске", 54.98 to 73.32),
    City("110", "Пермь", "Перми", 58.02 to 56.26),
    City("119", "Ростов-на-дону", "Ростове-на-дону", 47.23 to 39.70),
    City("123", "Самара", "Самаре", 53.24 to 50.22),
    City("151", "Уфа", "Уфе", 54.73 to 55.97),
    City("158", "Челябинск", "Челябинске", 55.16 to 61.43)
)

class GetCities : () -> Single<List<City>> {

    override fun invoke(): Single<List<City>> {
        return Single.just(inMemoryCities)
    }
}

class GetCityById : (CityId) -> Single<City> {

    override fun invoke(cityId: CityId): Single<City> {
        return Single.just(inMemoryCities.firstOrNull { it.id == cityId })
    }
}

data class City(
    val id: String,
    val title: String,
    val inclined: String,
    val latlng: Pair<Double, Double> = 0.0 to 0.0
)
