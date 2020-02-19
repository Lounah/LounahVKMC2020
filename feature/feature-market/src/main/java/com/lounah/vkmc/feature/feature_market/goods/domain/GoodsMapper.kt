package com.lounah.vkmc.feature.feature_market.goods.domain

import com.lounah.vkmc.core.core_vk.model.Product
import com.lounah.vkmc.feature.feature_market.goods.ui.recycler.ProductUi

internal class GoodsMapper : (List<Product>) -> List<ProductUi> {

    override fun invoke(products: List<Product>): List<ProductUi> {
        return products.map {
            ProductUi(
                uid = it.id,
                name = it.title,
                photo = it.photo,
                price = it.price.formattedAmount
            )
        }
    }
}