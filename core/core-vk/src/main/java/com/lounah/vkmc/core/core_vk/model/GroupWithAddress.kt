package com.lounah.vkmc.core.core_vk.model

data class GroupWithAddress(
    val id: String,
    val title: String,
    val address: String,
    val description: String,
    val photo: String,
    val latitude: Double,
    val longitude: Double
)

data class GroupAddressesResponse(
    val response: GroupAddresses
) {

    data class GroupAddresses(val items: List<GroupAddress>)
}

data class GroupAddress(
    val id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
