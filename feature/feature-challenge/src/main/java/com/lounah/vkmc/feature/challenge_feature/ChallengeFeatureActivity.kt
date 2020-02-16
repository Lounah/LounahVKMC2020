package com.lounah.vkmc.feature.challenge_feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.core.ui.util.ClickLock
import com.lounah.vkmc.core.ui.util.throttledClick
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerActivity
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_challenge_feature.*

class ChallengeFeatureActivity : AppCompatActivity() {

    private val clickLock = ClickLock()

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_feature)
        btnPicker.throttledClick(clickLock) {
            ImagePickerActivity.start(this, 1)
        }
    }

    companion object {
        val authScopes = emptyList<VKScope>()

        fun start(context: Context) =
            Intent(context, ChallengeFeatureActivity::class.java).also(context::startActivity)
    }
}
