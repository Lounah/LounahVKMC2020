package com.lounah.vkmc.feature.feature_map.di

import com.lounah.vkmc.core.core_vk.domain.groups.GetExtendedGroupInfo
import com.lounah.vkmc.core.core_vk.domain.groups.GetGroupsWithAddresses
import com.lounah.vkmc.core.core_vk.domain.photos.GetPhotosByLatLng
import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventDetailsPresenterFactory
import com.lounah.vkmc.feature.feature_map.map.business.GetNearestCity
import com.lounah.vkmc.feature.feature_map.map.domain.GetMapItems
import com.lounah.vkmc.feature.feature_map.map.domain.GroupsMapper
import com.lounah.vkmc.feature.feature_map.map.domain.PhotosMapper
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapPresenter

interface EventsMapComponent {
    val eventsMapPresenter: EventsMapPresenter
    val groupOrEventPresenterFactory: GroupOrEventDetailsPresenterFactory
}

fun EventsMapComponent() : EventsMapComponent = object : EventsMapComponent {

    private val getNearestCity = GetNearestCity()
    private val getMapItems = GetMapItems(
        GetGroupsWithAddresses(),
        GetPhotosByLatLng(),
        PhotosMapper(),
        GroupsMapper()
    )

    override val eventsMapPresenter: EventsMapPresenter = EventsMapPresenter(getNearestCity, getMapItems)

    override val groupOrEventPresenterFactory: GroupOrEventDetailsPresenterFactory
        = GroupOrEventDetailsPresenterFactory(GetExtendedGroupInfo())
}
