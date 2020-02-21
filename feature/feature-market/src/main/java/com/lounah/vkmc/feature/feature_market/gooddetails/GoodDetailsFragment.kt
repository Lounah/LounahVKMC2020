package com.lounah.vkmc.feature.feature_market.gooddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_market.R
import kotlinx.android.synthetic.main.fragment_good_details.*

internal class GoodDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_good_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments!!.getString(ARG_ID)
        val photo = arguments!!.getString(ARG_PHOTO).orEmpty()
        val productName = arguments!!.getString(ARG_NAME).orEmpty()
        val productPrice = arguments!!.getString(ARG_PRICE).orEmpty()
        title.text = productName
        name.text = productName
        price.text = productPrice
        image.load(photo)
    }

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_PHOTO = "photo"
        private const val ARG_ID = "id"
        private const val ARG_PRICE = "price"

        fun newInstance(id: String, name: String, photo: String, price: String): GoodDetailsFragment {
            return GoodDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_ID, id)
                    putString(ARG_PHOTO, photo)
                    putString(ARG_PRICE, price)
                }
            }
        }
    }
}