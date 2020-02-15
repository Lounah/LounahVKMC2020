package com.lounah.vkmc.login

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.*
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.R
import com.lounah.vkmc.core.extensions.toast
import com.lounah.vkmc.feature.challenge_feature.ChallengeFeatureActivity
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.LazyThreadSafetyMode.*

internal class LoginActivity : AppCompatActivity() {

    private val authCallback: VKAuthCallback by lazy(NONE) {
        object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) = startMainActivity()
            override fun onLoginFailed(errorCode: Int) = toast(R.string.login_auth_failed)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        checkIfLoggedIn()
        setContentView(R.layout.activity_login)
        loginBtn.setOnClickListener {
            VK.login(this, ChallengeFeatureActivity.authScopes)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && !VK.onActivityResult(requestCode, resultCode, data, authCallback))
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkIfLoggedIn() {
        if (VK.isLoggedIn()) {
            startMainActivity()
            finish()
        }
    }

    private fun startMainActivity() {
        ChallengeFeatureActivity.start(this)
        finish()
    }

    companion object {
        fun start(context: Context) = Intent(context, LoginActivity::class.java)
            .apply { flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP }
            .also(context::startActivity)
    }
}
