package com.lounah.vkmc.feature.image_viewer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.hide
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.ui.load
import com.lounah.vkmc.feature.image_viewer.R
import com.lounah.vkmc.feature.image_viewer.core.ContainerGestureListener
import com.lounah.vkmc.feature.image_viewer.core.ContentSizeProvider
import com.lounah.vkmc.feature.image_viewer.core.DismissableCallback
import kotlinx.android.synthetic.main.item_image.view.*

internal class ImageViewerViewPagerAdapter(
    private val images: ArrayList<String>,
    private val context: Context,
    private val onClickCallback: () -> Unit = {},
    private val onMoveCallback: (Float) -> Unit = {},
    private val dismissCallback: () -> Unit = {}
) : PagerAdapter() {

    private val contentHeightProvider = object : ContentSizeProvider {
        override fun heightForDismissAnimation() = 240.dp(context)
        override fun heightForCalculatingDismissThreshold() = 240.dp(context)
    }

    private val dismissCallbacks = object : DismissableCallback {
        override fun onFlickDismiss(flickAnimationDuration: Long) = dismissCallback()
        override fun onMove(moveRatio: Float) = onMoveCallback(moveRatio)
    }

    private val containerGestureListenerProvider
            = ContainerGestureListener(context, contentHeightProvider, dismissCallbacks)

    private val dismissGestureListener = containerGestureListenerProvider()

    override fun getCount() = images.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
        container.removeView(obj as View)

    override fun getPageTitle(position: Int): CharSequence = (position + 1).toString()

    override fun instantiateItem(container: ViewGroup, position: Int): View =
        LayoutInflater.from(container.context)
            .inflate(R.layout.item_image, container, false)
            .apply {
                imageContainer.gestureListener = dismissGestureListener
                image.load(images[position], success = {
                    image.show()
                    progress.hide()
                })
                image.setOnClickListener { onClickCallback() }
            }.also(container::addView)
}