package com.lounah.vkmc.feature.feature_market.markets.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction.*
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias MarketsSideEffect = SideEffect<MarketsState, MarketsAction>

class MarketsFragmentPresenter(
    private val getMarketsByCity: (CityId, Offset) -> Single<List<Market>>,
    private val getCityById: (CityId) -> Single<City>,
    private val userGroupsMapper: (List<Market>) -> List<MarketUi>
) {

    private val inputRelay = PublishRelay.create<MarketsAction>()

    val input: Consumer<MarketsAction> = inputRelay
    val state: Observable<MarketsState> = inputRelay
        .reduxStore(
            initialState = MarketsState(),
            sideEffects = listOf(
                loadPagedMarkets(),
                initialLoading(),
                repeatLoadMarkets(),
                changeCityId(),
                handleCityChanges()
            ),
            reducer = MarketsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): MarketsSideEffect {
        return { _, state ->
            loadMarkets(0, state().cityId).mergeWith(
                getCityById(state().cityId).map(::OnCityLoaded)
            )
        }
    }

    private fun loadPagedMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<OnNextPage>().flatMap { loadMarkets(it.offset, state().cityId) }
        }
    }

    private fun handleCityChanges(): MarketsSideEffect {
        return { actions, _ ->
            actions.ofType<OnCityIdChanged>().flatMap { loadMarkets(0, it.cityId) }
        }
    }

    private fun repeatLoadMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<OnRetryLoadingClicked>()
                .flatMap { loadMarkets(state().pageOffset, state().cityId) }
        }
    }

    private fun changeCityId(): MarketsSideEffect {
        return { actions, _ ->
            actions.ofType<ChangeCityId>().flatMap {
                getCityById(it.cityId).map<MarketsAction>(::OnCityLoaded)
                    .toObservable()
            }
        }
    }

    private fun loadMarkets(offset: Int, cityId: CityId): Observable<MarketsAction> {
        return getMarketsByCity(cityId, offset)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<MarketsAction> { OnMarketsLoaded(userGroupsMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
