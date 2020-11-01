package com.lounah.vkmc.core.core_vk.business.commands.user

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetUserCommand(
    override val method: String = "users.get",
    override val arguments: Map<String, String> = emptyMap(),
    override val responseParser: VKApiResponseParser<User> = GetUserResponseParser()
) : VKApiCommandWrapper<User>() {

    private class GetUserResponseParser(private val gson: Gson = Gson()) : VKApiResponseParser<User> {
        override fun parse(response: String?): User {
            return gson.fromJson(response, GetUserResponse::class.java).response.first()
        }
    }
}

@Keep
data class GetUserResponse(val response: List<User>)

@Keep
data class User(
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String
)
