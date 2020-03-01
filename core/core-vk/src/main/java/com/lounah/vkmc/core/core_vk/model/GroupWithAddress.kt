package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep

@Keep
data class GroupWithAddress(
    val id: String,
    val title: String,
    val address: String,
    val description: String,
    val photo: String,
    val latitude: Double,
    val longitude: Double
)

@Keep
data class GroupAddressesResponse(
    val response: GroupAddresses
) {

    @Keep
    data class GroupAddresses(val items: List<GroupAddress>)
}

@Keep
data class GroupAddress(
    val id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
