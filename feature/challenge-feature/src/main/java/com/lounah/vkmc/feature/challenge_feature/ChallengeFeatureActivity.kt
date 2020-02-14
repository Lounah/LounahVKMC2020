package com.lounah.vkmc.feature.challenge_feature

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.api.sdk.auth.VKScope

class ChallengeFeatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_feature)
    }

    companion object {
        val authScopes = emptyList<VKScope>()

        fun start(context: Context) =
            Intent(context, ChallengeFeatureActivity::class.java)
                .apply { flags = FLAG_ACTIVITY_NO_ANIMATION }
                .also(context::startActivity)
    }
}
