package com.lounah.vkmc.feature_places.places.preview.ui.recycler

import android.view.View
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import kotlinx.android.synthetic.main.item_story_preview.*

internal class StoryPreviewViewHolder(
    private val view: View, onClicked: (StoryUi) -> Unit
) : BaseViewHolder2<StoryUi>(view, onClicked) {

    override fun bind(item: StoryUi) {
        super.bind(item)
        title.text = item.title
        location.text = item.cityName
        views.text = item.views.toString()
        thumbnail.clipToOutline = true
        GlideApp.with(view.context)
            .load(item.thumbnailUri)
            .into(thumbnail)
    }
}
