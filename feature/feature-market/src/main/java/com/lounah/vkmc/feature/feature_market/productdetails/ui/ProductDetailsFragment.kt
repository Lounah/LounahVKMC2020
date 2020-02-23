package com.lounah.vkmc.feature.feature_market.productdetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.core_vk.domain.MarketId
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsPresenter
import com.lounah.vkmc.feature.feature_market.productdetails.di.ProductDetailsComponent
import com.lounah.vkmc.feature.feature_market.productdetails.presentation.ProductDetailsAction.OnFavButtonClicked
import com.lounah.vkmc.feature.feature_market.productdetails.presentation.ProductDetailsState
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.ProductUi
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlin.LazyThreadSafetyMode.NONE

internal class ProductDetailsFragment : Fragment() {

    private val presenter: ProductDetailsPresenter by lazy(NONE) {
        val productId = arguments!!.getParcelable<ProductUi>(ARG_PRODUCT)!!.uid
        getComponent<ProductDetailsComponent>().presenterFactory(productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_product_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.navigationBarColor = getColor(requireContext(), R.color.uikit_white_6)
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initBindings()
    }

    private fun initUi() {
        val product = arguments!!.getParcelable<ProductUi>(ARG_PRODUCT)!!
        title.text = product.name
        name.text = product.name
        price.text = product.price
        description.text = product.description
        image.load(
            product.photo,
            RequestOptions().placeholder(R.drawable.placeholder_product_details)
        )
        progressButton.run {
            setButtonTextColor(context!!.resources.getColorStateList(R.color.selector_fav_button))
            setButtonBackground(getDrawable(context!!, R.drawable.selector_button_add_to_fav)!!)
            setButtonBackgroundColor(getColor(context!!, R.color.uikit_white_6))
        }
        initClickListeners(product.photo)
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: ProductDetailsState) {
        progressButton.loading = state.isLoading
        progressButton.buttonSelected = state.isAddedToFav
        progressButton.text = if (state.isAddedToFav) {
            getString(R.string.remove_from_fav)
        } else getString(R.string.add_to_fav)
    }

    private fun initClickListeners(productPhoto: String) {
        progressButton.setButtonClickListener {
            presenter.input.accept(OnFavButtonClicked(progressButton.buttonSelected))
        }
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        image.setOnClickListener {
            ImageViewerActivity.start(requireContext(), arrayListOf(productPhoto))
        }
    }

    companion object {
        private const val ARG_PRODUCT = "product"
        private const val ARG_MARKET_ID = "market_id"

        fun newInstance(product: ProductUi, marketId: MarketId): ProductDetailsFragment {
            return ProductDetailsFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PRODUCT, product)
                        putString(ARG_MARKET_ID, marketId)
                    }
                }
        }
    }
}
