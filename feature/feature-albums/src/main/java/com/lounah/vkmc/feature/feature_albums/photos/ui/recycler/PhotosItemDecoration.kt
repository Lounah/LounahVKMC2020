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
        val firstInARow = position % 3 == 1
        val lastInARow = position % 3 == 0

        with(outRect) {
            if (position % 3 == 2) {
                left = 2.dp(view.context)
                right = 2.dp(view.context)
            }
//            right = 1.dp(view.context)
//            left = 1.dp(view.context)
//            bottom = 2.dp(view.context)
//            when {
//                firstInARow -> {
//                    bottom = 2.dp(view.context)
//                }
//                lastInARow -> {
//                    left = 2.dp(view.context)
//                }
//                else -> {
////                    left = 1.dp(view.context)
////                    right = 1.dp(view.context)
//                    left = 1.dp(view.context)
//                }
//            }
        }
    }
}

/*

//            right = 1.dp(view.context)
//            left = 1.dp(view.context)
//            bottom = 2.dp(view.context)

            when {
                firstInARow -> {
                    bottom = 2.dp(view.context)
                    right = 1.dp(view.context)
                }
                lastInARow -> {
                    left = 1.dp(view.context)
                }
                else -> {
//                    left = 1.dp(view.context)
//                    right = 1.dp(view.context)
                    left = 1.dp(view.context)
                }
            }
 */