package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.di

import android.content.Context
import com.lounah.vkmc.core.core_vk.domain.GetExtendedGroupInfo
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain.ExtendedGroupDetailsMapper
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain.NiceDateFormatter
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain.NiceNumbersFormatter
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsPresenterCreator

interface GroupDetailsDependencies {
    val appContext: Context
}

interface GroupDetailsComponent {
    val presenterCreator: GroupDetailsPresenterCreator
}

fun GroupDetailsComponent(
    deps: GroupDetailsDependencies
): GroupDetailsComponent = object : GroupDetailsComponent {

    private val groupDetailsMapper =
        ExtendedGroupDetailsMapper(NiceDateFormatter(), NiceNumbersFormatter(), deps.appContext)

    override val presenterCreator: GroupDetailsPresenterCreator =
        GroupDetailsPresenterCreator(GetExtendedGroupInfo(), groupDetailsMapper)
}