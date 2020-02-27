package com.lounah.vkmc.feature.feature_albums.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.feature.feature_albums.OnBackPressedListener
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.ui.AlbumsFragment
import com.vk.api.sdk.auth.VKScope.PHOTOS
import com.vk.api.sdk.auth.VKScope.WALL

class AlbumsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AlbumsFragment())
                .commit()
        }
    }

    override fun onBackPressed() {
        val currFragment = supportFragmentManager.fragments.firstOrNull()
        if ((currFragment as? OnBackPressedListener)?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }

    companion object {
        val authScopes = listOf(WALL, PHOTOS)

        fun start(context: Context) =
            Intent(context, AlbumsActivity::class.java).also(context::startActivity)
    }
}
