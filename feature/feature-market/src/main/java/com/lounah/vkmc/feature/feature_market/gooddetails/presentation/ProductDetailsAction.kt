package com.lounah.vkmc.feature.feature_market.gooddetails.presentation

sealed class ProductDetailsAction {
    class OnFavButtonClicked(val isAddedToFav: Boolean) : ProductDetailsAction()
    class OnFavStateLoaded(val isProductSaved: Boolean) : ProductDetailsAction()
    object OnFavStateChangeSuccess : ProductDetailsAction()
}