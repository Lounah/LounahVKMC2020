package com.lounah.vkmc.feature.feature_market.markets.di

import com.lounah.vkmc.core.core_vk.domain.GetCityById
import com.lounah.vkmc.core.core_vk.domain.market.GetMarketsByCity
import com.lounah.vkmc.feature.feature_market.markets.domain.UserGroupsMapper
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsFragmentPresenter

interface MarketsComponent {
    val presenter: MarketsFragmentPresenter
}

fun MarketsComponent(): MarketsComponent = object : MarketsComponent {
    override val presenter: MarketsFragmentPresenter =
        MarketsFragmentPresenter(GetMarketsByCity(), GetCityById(), UserGroupsMapper())
}
