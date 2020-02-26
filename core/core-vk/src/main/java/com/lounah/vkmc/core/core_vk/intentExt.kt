package com.lounah.vkmc.core.core_vk

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.vkViewIntent(subject: String) {
    Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse("https://vk.com/$subject")
    }.also(this::startActivity)
}

fun Context.vkViewWallPostIntent(ownerId: String, postId: String) =
    vkViewIntent("id${ownerId}?w=wall${ownerId}_${postId}")
