package com.lounah.vkmc.feature_places.camera.ui

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.LocationManager
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.lounah.vkmc.core.arch.lifecycle.bind
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.*
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.core.ui.bottomsheet.bottomSheetBehaviour
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsEvent.*
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsNews
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsNews.CloseDialog
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsNews.ShowError
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsState
import com.lounah.vkmc.feature_places.di.ClipsPlacesComponent
import kotlinx.android.synthetic.main.dialog_story_settings.*
import java.io.File
import kotlin.LazyThreadSafetyMode.NONE


internal class StorySettingsDialog : BaseBottomSheetDialogFragment(R.layout.dialog_story_settings) {

    companion object {
        private const val ARG_STORY_PATH = "arg_story_path"

        fun newInstance(storyPath: String) = StorySettingsDialog().apply {
            arguments = bundleOf(ARG_STORY_PATH to storyPath)
        }
    }

    private val locationManager by lazy(NONE) {
        requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
    }
    private val geocoder by lazy(NONE) { Geocoder(requireContext()) }
    private val storyPath by lazy(NONE) { requireArguments().getString(ARG_STORY_PATH) }

    private val store by lazy(NONE) { getComponent<ClipsPlacesComponent>().storySettingsStore }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    @SuppressLint("MissingPermission")
    private fun initUi() {
        store.bind(viewLifecycleOwner, ::render, ::handleNews)
        initClickListeners()
        loadStoryThumbnail()
        val location = locationManager.getLastKnownLocation(NETWORK_PROVIDER)
        store.dispatch(OnLocationObtained(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
    }

    private fun render(state: StorySettingsState) {
        dialog?.bottomSheetBehaviour?.isHideable = !state.isLoading
        if (state.isLoading) {
            actionButton.text = ""
            progressBar.show()
        } else {
            progressBar.hide()
            actionButton.setText(R.string.publish)
        }
        val addresses = geocoder.getFromLocation(state.lat, state.long, 1)
        location.text = addresses[0].thoroughfare
    }

    private fun handleNews(news: StorySettingsNews) {
        when (news) {
            is CloseDialog -> {
                toast(R.string.story_published)
                parentFragment.asType<CameraFragment>().enableVideoRecordingMode()
                dismiss()
            }
            is ShowError -> {
                toast(R.string.error_publishing)
            }
        }
    }

    private fun loadStoryThumbnail() {
        GlideApp.with(requireContext())
            .load(storyPath)
            .into(selectedImage)
    }

    private fun initClickListeners() {
        selectedImage.setOnClickListener {
            ImageViewerActivity.start(requireContext(), arrayListOf(storyPath.orEmpty()))
        }
        title.onTextChange { store.dispatch(OnTitleChanged(it)) }
        comment.onTextChange { store.dispatch(OnCommentChanged(it)) }
        actionButton.setOnClickListener { store.dispatch(OnPublishClicked(File(storyPath.orEmpty()))) }
    }
}
