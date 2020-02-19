package com.lounah.vkmc.feature.feature_market.cities.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.cities.di.CitiesListComponent
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListAction.InitialLoading
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListAction.OnCitySelected
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListPresenter
import com.lounah.vkmc.feature.feature_market.cities.presentation.CitiesListState
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CitiesAdapter
import com.lounah.vkmc.feature.feature_market.cities.ui.recycler.CityUi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cities_list.*
import kotlin.LazyThreadSafetyMode.NONE

internal class CitiesListFragment :
    BaseBottomSheetDialogFragment(R.layout.fragment_cities_list, expandByDefault = false) {

    private val citiesAdapter: CitiesAdapter by lazy(NONE) {
        CitiesAdapter(::onCityClicked)
    }

    private val presenter: CitiesListPresenter by lazy(NONE) {
        getComponent<CitiesListComponent>().citiesListPresenterFactory(argsString(ARG_CITY_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initBindings()
        closeBtn.setOnClickListener { dismiss() }
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = citiesAdapter
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: CitiesListState) {
        citiesAdapter.setItems(state.cities)
    }

    private fun onCityClicked(city: CityUi) {
        presenter.input.accept(OnCitySelected(city.uid))
    }

    override fun onDestroy() {
        super.onDestroy()
        val selectedCity = citiesAdapter.items.first { it.asType<CityUi>().isChecked }
        if (selectedCity.uid != argsString(ARG_CITY_ID)) {
            Intent().apply {
                putExtra(SELECTED_CITY, selectedCity.uid)
            }.also {
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, it)
            }
        }
    }

    companion object {
        const val SELECTED_CITY = "SELECTED_CITY"
        private const val ARG_CITY_ID = "city_id"

        fun newInstance(cityId: CityId): CitiesListFragment {
            return CitiesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY_ID, cityId)
                }
            }
        }
    }
}
