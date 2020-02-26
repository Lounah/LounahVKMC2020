package com.lounah.vkmc.feature.feature_market.productdetails.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.ProductId
import com.lounah.vkmc.feature.feature_market.data.FavProductsDao
import com.lounah.vkmc.feature.feature_market.productdetails.presentation.ProductDetailsAction.*
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType

typealias ProductDetailsSideEffect = SideEffect<ProductDetailsState, ProductDetailsAction>

class ProductDetailsPresenterFactory(
    private val productsDao: FavProductsDao
) : (ProductId) -> ProductDetailsPresenter {
    override fun invoke(productId: String): ProductDetailsPresenter {
        return ProductDetailsPresenter(
            productId,
            productsDao
        )
    }
}

class ProductDetailsPresenter(
    private val productId: String,
    private val productsDao: FavProductsDao
) {

    private val inputRelay = PublishRelay.create<ProductDetailsAction>()

    val input: Consumer<ProductDetailsAction> = inputRelay
    val state: Observable<ProductDetailsState> = inputRelay
        .reduxStore(
            initialState = ProductDetailsState(),
            sideEffects = listOf(initialLoading(), changeFavState()),
            reducer = ProductDetailsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): ProductDetailsSideEffect {
        return { _, _ ->
            productsDao.contains(productId)
                .toObservable()
                .map(::OnFavStateLoaded)
        }
    }

    private fun changeFavState(): ProductDetailsSideEffect {
        return { actions, _ ->
            actions.ofType<OnFavButtonClicked>().switchMap {
                productsDao.run { if (it.isAddedToFav) remove(productId) else add(productId) }
                    .onErrorComplete()
                    .andThen(Observable.just(OnFavStateChangeSuccess))
            }
        }
    }
}
