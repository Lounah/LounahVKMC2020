package com.lounah.vkmc.feature.feature_unsubscribe.ui

import androidx.recyclerview.widget.GridLayoutManager

class UserGroupsSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int = if (position == 0) 3 else 1
}