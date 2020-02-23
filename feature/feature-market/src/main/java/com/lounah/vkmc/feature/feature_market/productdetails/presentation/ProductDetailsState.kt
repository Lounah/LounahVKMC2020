package com.lounah.vkmc.feature.feature_market.productdetails.presentation

import com.lounah.vkmc.feature.feature_market.productdetails.presentation.ProductDetailsAction.*

data class ProductDetailsState(
    val isAddedToFav: Boolean = false,
    val isLoading: Boolean = false
)

internal fun ProductDetailsState.reduce(action: ProductDetailsAction): ProductDetailsState {
    return when (action) {
        is OnFavStateChangeSuccess -> copy(isAddedToFav = !isAddedToFav, isLoading = false)
        is OnLoadingStarted -> copy(isLoading = true)
        is OnError -> copy(isLoading = false)
        is OnFavStateLoaded -> copy(isAddedToFav = action.isProductSaved, isLoading = false)
        else -> this
    }
}
