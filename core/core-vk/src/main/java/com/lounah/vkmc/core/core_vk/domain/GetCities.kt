package com.lounah.vkmc.core.core_vk.domain

import io.reactivex.Single

private val inMemoryCities = listOf(
    City("1", "Москва", "Москве"),
    City("2", "Санкт-Петербург", "Санкт-Петербурге"),
    City("10", "Волгоград", "Волгограде"),
    City("37", "Владивосток", "Владивостоке"),
    City("153", "Хабаровск", "Хабаровске"),
    City("49", "Екатеринбург", "Екатеринбурге"),
    City("60", "Казань", "Казани"),
    City("61", "Калининград", "Калининграде"),
    City("72", "Краснодар", "Краснодаре"),
    City("73", "Красноярск", "Красноярске"),
    City("95", "Нижний новгород", "Нижнем новгороде"),
    City("99", "Новосибирск", "Новосибирске"),
    City("104", "Омск", "Омске"),
    City("110", "Пермь", "Перми"),
    City("119", "Ростов-на-дону", "Ростове-на-дону"),
    City("123", "Самара", "Самаре"),
    City("151", "Уфа", "Уфе"),
    City("158", "Челябинск", "Челябинске")
)

class GetCities : () -> Single<List<City>> {

    override fun invoke(): Single<List<City>> {
        return Single.just(inMemoryCities)
    }
}

data class City(
    val id: String,
    val title: String,
    val inclined: String
)
