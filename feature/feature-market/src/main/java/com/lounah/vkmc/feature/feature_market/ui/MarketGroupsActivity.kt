package com.lounah.vkmc.feature.feature_market.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.auth.VKScope.*

class MarketGroupsActivity : AppCompatActivity() {

    companion object {
        val authScopes = listOf(MARKET, ADS)

        fun start(context: Context) {
            Intent(context, MarketGroupsActivity::class.java).also(context::startActivity)
        }
    }
}
