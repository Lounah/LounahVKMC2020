package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain

import java.text.SimpleDateFormat
import java.util.*


internal class NiceDateFormatter(
    private val monthFormatter: SimpleDateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
) : (Long) -> String {

    override fun invoke(timestamp: Long): String {
        val unixtimeMs = timestamp * 1000
        return monthFormatter.format(Date(unixtimeMs))
    }
}
