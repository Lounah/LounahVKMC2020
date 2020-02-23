package com.lounah.vkmc.feature.feature_market.gooddetails.presentation

import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsAction.OnFavStateChangeSuccess
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsAction.OnFavStateLoaded

data class ProductDetailsState(
    val isAddedToFav: Boolean = false
)

internal fun ProductDetailsState.reduce(action: ProductDetailsAction): ProductDetailsState {
    return when (action) {
        is OnFavStateChangeSuccess -> copy(isAddedToFav = !isAddedToFav)
        is OnFavStateLoaded -> copy(isAddedToFav = action.isProductSaved)
        else -> this
    }
}