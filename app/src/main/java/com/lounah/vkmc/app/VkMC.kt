package com.lounah.vkmc.app

import android.app.Application
import com.lounah.vkmc.login.LoginActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

@Suppress("unused")
internal class VkMC : Application() {

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            LoginActivity.start(this@VkMC)
        }
    }

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }
}
