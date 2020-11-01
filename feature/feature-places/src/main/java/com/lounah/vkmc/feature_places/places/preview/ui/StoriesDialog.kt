package com.lounah.vkmc.feature_places.places.preview.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.recyclerview.widget.LinearLayoutManager
import com.lounah.vkmc.core.arch.util.subscribeWhen
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.di.ClipsPlacesComponent
import com.lounah.vkmc.feature_places.places.feed.host.ui.StoriesFeedActivity
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryPreviewAdapter
import kotlinx.android.synthetic.main.fragment_stories_list.*
import java.util.concurrent.TimeUnit
import kotlin.LazyThreadSafetyMode.NONE

internal class StoriesDialog : BaseBottomSheetDialogFragment(
    R.layout.fragment_stories_list,
    expandByDefault = false
) {

    companion object {
        private const val EXTRA_STORIES_IDS = "extra_stories_ids"

        fun newInstance(storiesIds: List<String>) = StoriesDialog().apply {
            arguments =
                bundleOf(EXTRA_STORIES_IDS to storiesIds.toTypedArray())
        }
    }

    private val storiesIds by lazy(NONE) { requireArguments().getStringArray(EXTRA_STORIES_IDS) }
    private val presenter by lazy(NONE) { getComponent<ClipsPlacesComponent>().storiesDialogPresenter }
    private val storiesAdapter by lazy(NONE) {
        StoryPreviewAdapter { selectedStory ->
            StoriesFeedActivity.start(
                requireContext(),
                storiesIds ?: emptyArray(),
                selectedStory.id
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initBindings()
    }

    private fun initUi() {
        recycler.run {
            adapter = storiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initBindings() {
        presenter.getPreviews(storiesIds.orEmpty().toList())
            .delay(1, TimeUnit.SECONDS)
            .subscribeWhen(lifecycle, STARTED, onNext = storiesAdapter::setItems)
    }
}
