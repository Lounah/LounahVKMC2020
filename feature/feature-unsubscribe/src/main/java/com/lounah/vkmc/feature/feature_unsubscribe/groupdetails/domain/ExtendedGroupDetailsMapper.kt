package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain

import android.content.Context
import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.lounah.vkmc.feature.feature_unsubscribe.R
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.ExtendedGroupUi

internal class ExtendedGroupDetailsMapper(
    private val dateFormatter: (Long) -> String,
    private val numbersFormatter: (Int) -> String,
    private val context: Context
) : (ExtendedGroup) -> ExtendedGroupUi {

    override fun invoke(group: ExtendedGroup): ExtendedGroupUi {
        return ExtendedGroupUi(
            title = getSubscribersFullInfo(group.subscribers, group.friendsCount),
            name = group.name,
            screenName = group.screenName,
            description = group.description,
            wallPostDate = context.getString(
                R.string.wall_post_date,
                dateFormatter(group.lastPostDate)
            )
        )
    }

    private fun getSubscribersFullInfo(subscribersCount: Int, friendsCount: Int): String {
        return "${numbersFormatter(subscribersCount)} ${getSubsString(subscribersCount)} • ${numbersFormatter(
            friendsCount
        )} ${getFriendsString(friendsCount)}"
    }

    private fun getSubsString(subscribersCount: Int): String {
        return context.resources.getQuantityString(R.plurals.subscribersCount, subscribersCount)
    }

    private fun getFriendsString(friendsCount: Int): String {
        return context.resources.getQuantityString(R.plurals.friendsCount, friendsCount)
    }
}