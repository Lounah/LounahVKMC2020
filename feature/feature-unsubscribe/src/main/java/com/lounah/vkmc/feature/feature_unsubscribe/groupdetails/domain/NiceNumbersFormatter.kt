package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.domain

internal class NiceNumbersFormatter : (Int) -> String {

    override fun invoke(number: Int): String {
        return when {
            number < 1000 -> "$number"
            number in 1000..1_000_000 -> {
                val thousands = number / 1000
                val tail = (number % 1000) / 100
                val suff = if (tail != 0) ",$tail" else ""
                "${thousands}${suff}K"
            }
            else -> {
                val millions = number / 1_000_000
                val tail = (number % 1_000_000) / 100_000
                val suff = if (tail != 0) ",$tail" else ""
                "${millions}${suff}M"
            }
        }
    }
}