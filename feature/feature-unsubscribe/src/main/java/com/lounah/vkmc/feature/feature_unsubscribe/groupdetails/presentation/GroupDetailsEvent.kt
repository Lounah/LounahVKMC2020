package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation

sealed class GroupDetailsEvent {
    class OpenGroupDetails(val screenName: String) : GroupDetailsEvent()
}