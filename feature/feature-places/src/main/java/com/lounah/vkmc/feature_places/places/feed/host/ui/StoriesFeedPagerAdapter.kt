package com.lounah.vkmc.feature_places.places.feed.host.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lounah.vkmc.feature_places.places.feed.details.ui.StoryFragment
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi

internal class StoriesFeedPagerAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    val items = mutableListOf<StoryUi>()

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = StoryFragment.newInstance(items[position].id)

    fun itemPositionById(storyId: String) = items.indexOfFirst { it.id == storyId }

    fun setItems(stories: List<StoryUi>) {
        items.addAll(stories.filterNot(items::contains))
        notifyDataSetChanged()
    }
}
