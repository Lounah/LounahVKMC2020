package com.lounah.vkmc.feature.feature_market.goods.di

import com.lounah.vkmc.core.core_vk.domain.GetProductsByMarket
import com.lounah.vkmc.feature.feature_market.goods.domain.GoodsMapper
import com.lounah.vkmc.feature.feature_market.goods.presentation.MarketGoodsPresenterFactory

interface MarketGoodsComponent {
    val presenterFactory: MarketGoodsPresenterFactory
}

fun MarketGoodsComponent(): MarketGoodsComponent = object : MarketGoodsComponent {

    override val presenterFactory: MarketGoodsPresenterFactory
        = MarketGoodsPresenterFactory(GetProductsByMarket(), GoodsMapper())
}