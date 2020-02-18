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
