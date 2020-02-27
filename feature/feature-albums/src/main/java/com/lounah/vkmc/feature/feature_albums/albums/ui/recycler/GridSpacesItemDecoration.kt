package com.lounah.vkmc.feature.feature_albums.albums.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.dp

internal class GridSpacesDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            left = 6.dp(view.context)
            right = 6.dp(view.context)
        }
    }
}
