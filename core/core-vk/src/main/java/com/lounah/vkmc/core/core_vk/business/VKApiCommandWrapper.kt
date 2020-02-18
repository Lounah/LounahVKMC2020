package com.lounah.vkmc.core.core_vk.business

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand

internal abstract class VKApiCommandWrapper<T> : ApiCommand<T>() {

    abstract val method: String
    abstract val arguments: Map<String, String>
    abstract val responseParser: VKApiResponseParser<T>

    override fun onExecute(manager: VKApiManager): T {
        val call = VKMethodCall.Builder()
            .method(method)
            .args(arguments)
            .version(manager.config.version)
            .build()

        return manager.execute(call, responseParser)
    }
}

