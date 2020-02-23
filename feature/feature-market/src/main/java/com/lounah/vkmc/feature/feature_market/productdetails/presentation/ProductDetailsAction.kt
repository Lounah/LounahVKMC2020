package com.lounah.vkmc.feature.feature_market.productdetails.presentation

sealed class ProductDetailsAction {
    class OnFavButtonClicked(val isAddedToFav: Boolean) : ProductDetailsAction()
    class OnFavStateLoaded(val isProductSaved: Boolean) : ProductDetailsAction()
    object OnLoadingStarted : ProductDetailsAction()
    object OnFavStateChangeSuccess : ProductDetailsAction()
    object OnError : ProductDetailsAction()
}
