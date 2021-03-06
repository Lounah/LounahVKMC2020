package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GroupsResponse(
    val response: Groups
)

@Keep
data class Groups(
    val items: List<Group>
)

@Keep
data class Group(
    val id: String,
    val name: String,
    @SerializedName("photo_200")
    val photo: String
)

@Keep
data class GroupByIdResponse(
    val response: List<GroupById>
)

@Keep
data class GroupById(
    val id: Int,
    val name: String,
    @SerializedName("screen_name")
    val screenName: String,
    val description: String,
    @SerializedName("members_count")
    val subscribers: Int,
    @SerializedName("photo_medium")
    val photo: String
)

@Keep
data class ExtendedGroup(
    val id: String,
    val name: String,
    @SerializedName("screen_name")
    val screenName: String,
    val subscribers: Int,
    val friendsCount: Int,
    val description: String,
    val lastPostDate: Long
)
