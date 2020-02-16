package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WallPost(
    @SerializedName("post_id")
    val id: Int
)
