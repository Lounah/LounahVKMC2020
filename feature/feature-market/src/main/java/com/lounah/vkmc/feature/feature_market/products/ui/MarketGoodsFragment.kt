package com.lounah.vkmc.feature.feature_market.products.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lounah.vkmc.core.core_vk.domain.market.MarketId
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.productdetails.ui.ProductDetailsFragment
import com.lounah.vkmc.feature.feature_market.products.di.MarketGoodsComponent
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsAction.OnNextPage
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsAction.OnRetryLoadingClicked
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsPresenter
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsState
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.GridSpacesDecoration
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.ProductUi
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.ProductsAdapter
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.ProductsSpanSizeLookUp
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.fragment_products.*
import kotlin.LazyThreadSafetyMode.NONE

internal class MarketGoodsFragment : Fragment() {

    private val productsAdapter: ProductsAdapter by lazy(NONE) {
        ProductsAdapter(::onProductClicked, ::onRepeatPagedLoading)
    }

    private val marketId: String by lazy(NONE) {
        arguments?.getString(ARG_MARKET_ID).orEmpty()
    }

    private val presenter: MarketGoodsPresenter by lazy(NONE) {
        getComponent<MarketGoodsComponent>().presenterFactory(marketId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_products, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initBindings()
        val marketName = arguments?.getString(ARG_MARKET_NAME).orEmpty()
        title.text = getString(R.string.goods_of, marketName)
        closeBtn.setOnClickListener { activity!!.supportFragmentManager.popBackStack() }
    }

    private fun initBindings() {
        presenter.state
            .observeOn(mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: MarketGoodsState) {
        productsAdapter.setItems(state.goods)
    }

    private fun initRecycler() {
        recyclerView.apply {
            val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            lm.spanSizeLookup = ProductsSpanSizeLookUp { productsAdapter.items }
            layoutManager = lm
            addItemDecoration(GridSpacesDecoration())
            adapter = productsAdapter
            pagedScrollListener { presenter.input.accept(OnNextPage(it)) }
        }
    }

    private fun onProductClicked(product: ProductUi) {
        val details = ProductDetailsFragment.newInstance(product, marketId)
        activity!!.supportFragmentManager.beginTransaction()
            .add(R.id.container, details)
            .addToBackStack(null)
            .hide(this)
            .commit()
    }

    private fun onRepeatPagedLoading() {
        presenter.input.accept(OnRetryLoadingClicked)
    }

    companion object {
        private const val ARG_MARKET_ID = "market_id"
        private const val ARG_MARKET_NAME = "market_name"

        fun newInstance(marketId: MarketId, marketName: String): MarketGoodsFragment {
            return MarketGoodsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MARKET_ID, marketId)
                    putString(ARG_MARKET_NAME, marketName)
                }
            }
        }
    }
}
