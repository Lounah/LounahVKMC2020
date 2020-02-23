package com.lounah.vkmc.feature.feature_market.gooddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.core.ui.util.ClickLock
import com.lounah.vkmc.core.ui.util.throttledClick
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.gooddetails.di.ProductDetailsComponent
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsAction.OnFavButtonClicked
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsPresenter
import com.lounah.vkmc.feature.feature_market.gooddetails.presentation.ProductDetailsState
import com.lounah.vkmc.feature.feature_market.goods.ui.recycler.ProductUi
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_good_details.*
import kotlin.LazyThreadSafetyMode.NONE

internal class ProductDetailsFragment : Fragment() {

    private val actionButtonClickLock = ClickLock()

    private val presenter: ProductDetailsPresenter by lazy(NONE) {
        val productId = arguments!!.getParcelable<ProductUi>(ARG_PRODUCT)!!.uid
        getComponent<ProductDetailsComponent>().presenterFactory(productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_good_details, container, false)

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
        image.load(product.photo)
        addToFav.throttledClick(actionButtonClickLock) {
            presenter.input.accept(OnFavButtonClicked(addToFav.isSelected))
        }
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        image.setOnClickListener {
            ImageViewerActivity.start(requireContext(), arrayListOf(product.photo))
        }
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: ProductDetailsState) {
        addToFav.isSelected = state.isAddedToFav
        addToFav.text = if (state.isAddedToFav) {
            getString(R.string.remove_from_fav)
        } else getString(R.string.add_to_fav)
    }

    companion object {
        private const val ARG_PRODUCT = "product"

        fun newInstance(product: ProductUi): ProductDetailsFragment {
            return ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT, product)
                }
            }
        }
    }
}