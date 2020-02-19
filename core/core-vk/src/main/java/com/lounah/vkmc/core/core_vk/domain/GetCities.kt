package com.lounah.vkmc.core.core_vk.domain

import io.reactivex.Single

private val inMemoryCities = listOf(
    City("1", "Москва", "Москве" )
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
