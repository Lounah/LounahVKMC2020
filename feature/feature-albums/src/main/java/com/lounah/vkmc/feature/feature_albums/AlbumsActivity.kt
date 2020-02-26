package com.lounah.vkmc.feature.feature_albums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.auth.VKScope.PHOTOS
import com.vk.api.sdk.auth.VKScope.WALL

class AlbumsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_LAYOUT_NO_LIMITS)
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)
    }

    companion object {
        val authScopes = listOf(WALL, PHOTOS)

        fun start(context: Context) =
            Intent(context, AlbumsActivity::class.java).also(context::startActivity)
    }
}
