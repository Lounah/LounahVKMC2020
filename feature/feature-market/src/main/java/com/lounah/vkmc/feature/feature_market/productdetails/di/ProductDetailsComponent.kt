package com.lounah.vkmc.feature.feature_market.productdetails.di

import com.lounah.vkmc.feature.feature_market.data.FavProductSelectQueryEngine
import com.lounah.vkmc.feature.feature_market.data.FavProductsDaoImpl
import com.lounah.vkmc.feature.feature_market.data.core.DatabaseHelper
import com.lounah.vkmc.feature.feature_market.data.sql.InsertQueryEngine
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsPresenterFactory

interface ProductDetailsComponent {
    val presenterFactory: ProductDetailsPresenterFactory
}

fun ProductDetailsComponent(deps: ProductDetailsDependencies): ProductDetailsComponent =
    object : ProductDetailsComponent, ProductDetailsDependencies by deps {

        private val db = DatabaseHelper(deps.appContext).writableDatabase

        private val productsDao =
            FavProductsDaoImpl(db, FavProductSelectQueryEngine(), InsertQueryEngine())

        override val presenterFactory: ProductDetailsPresenterFactory =
            ProductDetailsPresenterFactory(productsDao)
    }
