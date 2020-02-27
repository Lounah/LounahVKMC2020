package com.lounah.vkmc.app

import android.app.Application
import com.lounah.vkmc.di.InjectorInitializer
import com.lounah.vkmc.login.LoginActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.exceptions.VKApiExecutionException
import io.reactivex.plugins.RxJavaPlugins
import java.io.InterruptedIOException

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
        InjectorInitializer.initialize(this)
        RxJavaPlugins.setErrorHandler {
            if (it is VKApiExecutionException) tokenTracker.onTokenExpired()
            if (it !is InterruptedException && it !is InterruptedIOException)
                return@setErrorHandler
        }
    }
}
