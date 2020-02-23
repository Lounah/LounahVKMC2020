package com.lounah.vkmc.feature.feature_market.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.markets.ui.MarketsFragment
import com.vk.api.sdk.auth.VKScope.*

class MarketGroupsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markets)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MarketsFragment())
                .commit()
        }
    }

    companion object {
        val authScopes = listOf(MARKET, ADS, NOTES, WALL)

        fun start(context: Context) {
            Intent(context, MarketGroupsActivity::class.java).also(context::startActivity)
        }
    }
}
