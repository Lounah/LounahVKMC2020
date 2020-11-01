package com.lounah.vkmc.feature_places.clips

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.setMarginTop
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.host.VKMainActivity
import kotlinx.android.synthetic.main.fragment_clips_host.*

class ClipsHostFragment : Fragment(R.layout.fragment_clips_host) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        addVerticalMargin()
        camera.setOnClickListener {
            requireActivity().asType<VKMainActivity>().navigateToPhotos()
        }
        yourStories.setOnClickListener {
            requireActivity().asType<VKMainActivity>().navigateToAlbums()
        }
    }

    private fun setupViewPager() {
        contentContainer.offscreenPageLimit = 1
        contentContainer.adapter =
            ClipsContentContainerAdapter(parentFragmentManager, requireContext()::getString)
        tabs.setupWithViewPager(contentContainer)
    }

    private fun addVerticalMargin() {
        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            toolbar.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
    }
}
