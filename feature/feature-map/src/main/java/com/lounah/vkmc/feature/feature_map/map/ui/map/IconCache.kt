package com.lounah.vkmc.feature.feature_map.map.ui.map

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import java.util.concurrent.ConcurrentHashMap

internal interface IconCache {
    val cache: Map<String, Bitmap>

    fun put(path: String, isPhoto: Boolean)

    fun get(path: String): Bitmap?

    companion object {
        operator fun invoke(context: Context): IconCacheImpl =
            IconCacheImpl(context)
    }
}

class IconCacheImpl(private val context: Context) :
    IconCache {

    override val cache: MutableMap<String, Bitmap> = ConcurrentHashMap()

    @Synchronized
    override fun put(path: String, isPhoto: Boolean) {
        if (cache[path] == null) {
            GlideApp.with(context).asBitmap().load(path)
                .also { if (!isPhoto) it.circleCrop() }
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        cache[path] = resource
                    }
                })
        }
    }

    override fun get(path: String): Bitmap? = cache[path]
}
