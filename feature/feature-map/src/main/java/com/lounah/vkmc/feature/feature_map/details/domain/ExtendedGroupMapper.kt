package com.lounah.vkmc.feature.feature_map.details.domain

import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.lounah.vkmc.feature.feature_map.details.ui.GroupOrEventUi

class ExtendedGroupMapper(
    private val groupAddress: String
) : (ExtendedGroup) -> GroupOrEventUi {

    override fun invoke(group: ExtendedGroup): GroupOrEventUi {
        return GroupOrEventUi(
            id = group.id,
            title = group.name,
            screenName = group.screenName,
            address = groupAddress,
            description = group.description
        )
    }
}
