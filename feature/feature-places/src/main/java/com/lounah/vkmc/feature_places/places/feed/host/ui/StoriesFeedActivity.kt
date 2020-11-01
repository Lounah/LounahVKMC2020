package com.lounah.vkmc.feature_places.places.feed.host.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle.State.STARTED
import com.lounah.vkmc.core.arch.util.subscribeWhen
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.transparentStatusbar
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature.image_viewer.ui.pageChange
import com.lounah.vkmc.feature_places.di.ClipsPlacesComponent
import kotlinx.android.synthetic.main.activity_stories_feed.*
import kotlin.LazyThreadSafetyMode.NONE


internal class StoriesFeedActivity : AppCompatActivity(R.layout.activity_stories_feed),
    OnVideoViewedCallback {

    companion object {
        private const val EXTRA_STORIES_IDS = "extra_stories_ids"
        private const val EXTRA_SELECTED_STORY = "extra_selected_story"

        fun start(context: Context, storiesIds: Array<String>, selectedStory: String) =
            Intent(context, StoriesFeedActivity::class.java)
                .putExtra(EXTRA_SELECTED_STORY, selectedStory)
                .putExtra(EXTRA_STORIES_IDS, storiesIds)
                .also(context::startActivity)
    }

    private val storiesIds by lazy(NONE) { intent.getStringArrayExtra(EXTRA_STORIES_IDS) }
    private val selectedStoryId by lazy(NONE) { intent.getStringExtra(EXTRA_SELECTED_STORY) }
    private val presenter by lazy(NONE) { getComponent<ClipsPlacesComponent>().storiesDialogPresenter }
    private lateinit var pagerAdapter: StoriesFeedPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
        super.onCreate(savedInstanceState)
        initUi()
        initBindings()
    }

    private fun initUi() {
        transparentStatusbar()
        pagerAdapter = StoriesFeedPagerAdapter(this)
        pager.offscreenPageLimit = 1
        pager.adapter = pagerAdapter
        pager.pageChange { titleView.text = pagerAdapter.items[it].title }
        back.setOnClickListener { onBackPressed() }
    }

    private fun initBindings() {
        presenter.getPreviews(storiesIds.orEmpty().toList())
            .subscribeWhen(lifecycle, STARTED, onNext = {
                pagerAdapter.setItems(it)
                pager.setCurrentItem(pagerAdapter.itemPositionById(selectedStoryId), false)
            })
    }

    override fun onVideoViewed(storyId: String) {
        pager.setCurrentItem(pager.currentItem + 1, true)
        presenter.onStoryIncrementViews(storyId).subscribeWhen(lifecycle, STARTED)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
    }
}

interface OnVideoViewedCallback {
    fun onVideoViewed(storyId: String)
}
