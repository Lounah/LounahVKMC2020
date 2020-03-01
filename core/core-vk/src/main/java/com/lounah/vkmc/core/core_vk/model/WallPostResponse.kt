package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WallPostResponse(
    val response: WallPostId
)

@Keep
data class WallPostId(
    @SerializedName("post_id")
    val id: String
)
