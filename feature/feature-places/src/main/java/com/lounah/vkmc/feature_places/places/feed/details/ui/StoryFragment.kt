package com.lounah.vkmc.feature_places.places.feed.details.ui

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.State.STARTED
import com.lounah.vkmc.core.arch.util.subscribeWhen
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.di.ClipsPlacesComponent
import com.lounah.vkmc.feature_places.places.feed.host.ui.OnVideoViewedCallback
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi
import kotlinx.android.synthetic.main.fragment_story.*
import kotlin.LazyThreadSafetyMode.NONE

internal class StoryFragment : Fragment(R.layout.fragment_story) {

    companion object {
        private const val EXTRA_STORY_ID = "extra_story_id"

        fun newInstance(storyId: String) = StoryFragment().apply {
            arguments = bundleOf(EXTRA_STORY_ID to storyId)
        }
    }

    private val component by lazy(NONE) { getComponent<ClipsPlacesComponent>() }
    private val presenter by lazy(NONE) { component.storiesDialogPresenter }
    private val storageRef by lazy(NONE) { component.storageReference }
    private val storyId by lazy(NONE) { requireArguments().getString(EXTRA_STORY_ID) }
    private lateinit var storyPlayer: StoryPlayer

    override fun onResume() {
        super.onResume()
        storyPlayer = StoryPlayerImpl(requireContext(), storageRef, ::onVideoViewed)
        presenter.getStory(storyId.orEmpty())
            .subscribeWhen(lifecycle, STARTED, onNext = ::renderStory)
    }

    override fun onPause() {
        super.onPause()
        storyPlayer.release()
    }

    private fun onVideoViewed() {
        activity?.run {
            asType<OnVideoViewedCallback>()
                .onVideoViewed(storyId.orEmpty())
        }
    }

    private fun renderStory(story: StoryUi) {
        author.text = story.author
        comment.text = story.comment
        views.text = story.views.toString()
        location.text = story.cityName
        storyPlayer.play(story, playerView, ::handleError)
    }

    private fun handleError(error: Throwable) {
        println("Error: $error")
    }
}
