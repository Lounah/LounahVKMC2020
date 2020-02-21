package com.lounah.vkmc.feature.feature_market.goods.domain

import com.lounah.vkmc.core.core_vk.model.Product
import com.lounah.vkmc.feature.feature_market.goods.ui.recycler.ProductUi
import java.text.NumberFormat

private val moneyFormatter = NumberFormat.getCurrencyInstance()

internal class GoodsMapper : (List<Product>) -> List<ProductUi> {

    override fun invoke(products: List<Product>): List<ProductUi> {
        return products.map {
            ProductUi(
                uid = it.id,
                name = it.title,
                photo = it.photo,
                price = "${getMoneyAmount(it.price)} ${getCurrencySign(it.price.currency)}"
            )
        }
    }

    private fun getMoneyAmount(price: Product.Price): String {
        val money = moneyFormatter.format(price.harmonizedAmount.toDouble())
        return money.take(money.lastIndex - 1)
            .replaceAfterLast(",", "")
            .replace(",", "")
    }

    private fun getCurrencySign(currency: Product.Currency): String {
        return when {
            currency.isEur -> "€"
            currency.isUsd -> "$"
            else -> "\u20BD"
        }
    }
}