package com.lounah.vkmc.core.ui.imageloader

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class VKMCGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        RequestOptions().apply {
            val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

            format(
                if (activityManager.isLowRamDevice) DecodeFormat.PREFER_RGB_565
                else DecodeFormat.PREFER_ARGB_8888
            )

            disallowHardwareConfig()

            builder.setDefaultRequestOptions(this)
        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
