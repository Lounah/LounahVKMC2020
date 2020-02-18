package com.lounah.vkmc.core.recycler.paging.core

import android.view.View
import com.lounah.vkmc.core.recycler.base.ViewTyped
import kotlinx.android.synthetic.main.item_error.*

class FullScreenErrorViewHolder(
    onRepeat: () -> Unit,
    containerView: View
) : BaseViewHolder2<ViewTyped>(containerView) {

    init {
        btnRepeat.setOnClickListener { onRepeat() }
    }
}
