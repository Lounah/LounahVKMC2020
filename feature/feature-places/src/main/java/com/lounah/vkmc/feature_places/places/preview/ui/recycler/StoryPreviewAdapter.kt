package com.lounah.vkmc.feature_places.places.preview.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.feature.feature_places.R

internal class StoryPreviewAdapter(
    private val onStoryClicked: (StoryUi) -> Unit
) : BaseAdapter() {

    init {
        itemsInternal.addAll(listOf(StoryLoadingUi, StoryLoadingUi))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder2<ViewTyped> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_story_preview -> StoryPreviewViewHolder(view, onStoryClicked).asType()
            R.layout.view_stories_preview_shimmer -> BaseViewHolder2(view)
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_story_preview -> (holder as StoryPreviewViewHolder).bind(item.asType())
        }
    }

    override fun setItems(items: List<ViewTyped>) {
        itemsInternal.clear()
        itemsInternal.addAll(items)
        notifyDataSetChanged()
    }
}
