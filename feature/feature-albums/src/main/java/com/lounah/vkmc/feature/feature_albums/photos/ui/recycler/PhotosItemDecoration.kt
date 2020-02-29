package com.lounah.vkmc.feature.feature_albums.photos.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.dp

internal class PhotosItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) {
            val column = (position - 1) % 3
            val spacing = 2.dp(view.context)
            with(outRect) {
                left = column * spacing / 3
                right = spacing - (column + 1) * spacing / 3

                if (position >= 4)
                    top = spacing
            }
        } else {
            outRect.top = 2.dp(view.context)
            outRect.bottom = 2.dp(view.context)
        }
    }
}
