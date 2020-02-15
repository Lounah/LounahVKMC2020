package com.lounah.vkmc.core.recycler.base.items

import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.ViewTyped

object ProgressItem : ViewTyped {
    override val uid: String = "PROGRESS_ITEM_ID"
    override val viewType: Int = R.layout.item_progress
}

data class ShimmerItem(override val viewType: Int) : ViewTyped {
    override val uid: String = "SHIMMER_ITEM_ID"
}

class SpaceItem(override val viewType: Int) : ViewTyped {
    override val uid: String = "SPACE_ITEM_ID"
}
