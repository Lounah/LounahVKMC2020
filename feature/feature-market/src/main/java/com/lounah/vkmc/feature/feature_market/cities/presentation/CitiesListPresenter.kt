package com.lounah.vkmc.feature.feature_market.cities.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListAction.OnCitiesLoaded
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CityUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer

private typealias CitiesListSideEffect = SideEffect<CitiesListState, CitiesListAction>

class CitiesListPresenterFactory(
    private val getCities: () -> Single<List<City>>,
    private val citiesMapper: (List<City>) -> List<CityUi>
) : (CityId) -> CitiesListPresenter {
    override fun invoke(cityId: CityId): CitiesListPresenter {
        return CitiesListPresenter(cityId, getCities, citiesMapper)
    }
}

class CitiesListPresenter(
    cityId: CityId,
    private val getCities: () -> Single<List<City>>,
    private val citiesMapper: (List<City>) -> List<CityUi>
) {
    private val inputRelay = PublishRelay.create<CitiesListAction>()

    val input: Consumer<CitiesListAction> = inputRelay
    val state: Observable<CitiesListState> = inputRelay
        .reduxStore(
            initialState = CitiesListState(selectedCity = cityId),
            sideEffects = listOf(initialLoading()),
            reducer = CitiesListState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): CitiesListSideEffect {
        return { _, _ ->
            getCities().map { OnCitiesLoaded(citiesMapper(it)) }
                .toObservable()
        }
    }
}
