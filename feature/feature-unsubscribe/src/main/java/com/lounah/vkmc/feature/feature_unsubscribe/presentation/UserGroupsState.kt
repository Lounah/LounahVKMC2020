package com.lounah.vkmc.feature.feature_unsubscribe.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped

data class UserGroupsState(
    val userGroups: List<ViewTyped> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false
)

