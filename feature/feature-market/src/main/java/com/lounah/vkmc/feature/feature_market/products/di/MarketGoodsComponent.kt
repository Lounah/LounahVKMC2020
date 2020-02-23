package com.lounah.vkmc.feature.feature_market.products.di

import com.lounah.vkmc.core.core_vk.domain.GetProductsByMarket
import com.lounah.vkmc.feature.feature_market.products.domain.GoodsMapper
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsPresenterFactory

interface MarketGoodsComponent {
    val presenterFactory: MarketGoodsPresenterFactory
}

fun MarketGoodsComponent(): MarketGoodsComponent = object : MarketGoodsComponent {

    override val presenterFactory: MarketGoodsPresenterFactory
        = MarketGoodsPresenterFactory(GetProductsByMarket(), GoodsMapper())
}
