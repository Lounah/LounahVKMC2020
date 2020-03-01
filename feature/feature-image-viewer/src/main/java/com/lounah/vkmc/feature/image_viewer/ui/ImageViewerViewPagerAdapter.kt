package com.lounah.vkmc.feature.image_viewer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.hide
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.image_viewer.R
import com.lounah.vkmc.feature.image_viewer.core.*
import kotlinx.android.synthetic.main.item_imageviewer_image.view.*

internal class ImageViewerViewPagerAdapter(
    private val context: Context,
    private val onClickCallback: () -> Unit = {},
    private val onMoveCallback: (Float) -> Unit = {},
    private val dismissCallback: () -> Unit = {}
) : PagerAdapter() {

    private val images: ArrayList<String> = ArrayList()

    private val contentHeightProvider = object : ContentSizeProvider {
        override fun heightForDismissAnimation() = 240.dp(context)
        override fun heightForCalculatingDismissThreshold() = 240.dp(context)
    }

    private val dismissCallbacks = object : DismissableCallback {
        override fun onFlickDismiss(flickAnimationDuration: Long) = dismissCallback()
        override fun onMove(moveRatio: Float) = onMoveCallback(moveRatio)
    }

    private val containerGestureListenerProvider =
        ContainerGestureListener(context, contentHeightProvider, dismissCallbacks)

    private val dismissGestureListener: (ImageDismissLayout) -> DismissGestureListener =
        { containerGestureListenerProvider(it) }

    fun addImages(new: List<String>) {
        images.addAll(new.filterNot(images::contains))
        notifyDataSetChanged()
    }

    override fun getCount() = images.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
        container.removeView(obj as View)

    override fun getPageTitle(position: Int): CharSequence = (position + 1).toString()

    override fun instantiateItem(container: ViewGroup, position: Int): View =
        LayoutInflater.from(container.context)
            .inflate(R.layout.item_imageviewer_image, container, false)
            .apply {
                imageContainer.gestureListener = dismissGestureListener(imageContainer)
                image.load(images[position], success = {
                    image.show()
                    progress.hide()
                })
                image.setOnClickListener { onClickCallback() }
            }.also(container::addView)
}
