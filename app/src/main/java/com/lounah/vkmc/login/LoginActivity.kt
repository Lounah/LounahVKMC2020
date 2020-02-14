package com.lounah.vkmc.login

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.R
import com.lounah.vkmc.core.extensions.toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.LazyThreadSafetyMode.*

internal class LoginActivity : AppCompatActivity() {

    private val authCallback: VKAuthCallback by lazy(NONE) {
        object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) = Unit
            override fun onLoginFailed(errorCode: Int) = toast(R.string.login_auth_failed)
        }
    }

    private val authScopes = emptyList<VKScope>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfLoggedIn()
        setContentView(R.layout.activity_splash)
        loginBtn.setOnClickListener { VK.login(this, authScopes) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && !VK.onActivityResult(requestCode, resultCode, data, authCallback))
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkIfLoggedIn() {
        if (VK.isLoggedIn()) {

        }
    }

    companion object {
        fun start(context: Context) = Intent(context, LoginActivity::class.java)
            .apply { flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP }
            .also(context::startActivity)
    }
}
