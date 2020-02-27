package com.lounah.vkmc.feature.feature_map.map.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Priority
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import com.lounah.vkmc.feature.feature_map.R

internal class PhotoMarkerView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs), MarkerView {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_map_photo, this, true)
    }

    override fun setIcon(path: String) {
        GlideApp.with(context).asBitmap().load(path).priority(Priority.IMMEDIATE)
            .placeholder(R.drawable.placeholder_cluster_photo)
            .override(48.dp(context), 48.dp(context)).into(findViewById(R.id.icon))
    }

    override fun setImage(image: Bitmap) {
        findViewById<ImageView>(R.id.icon).setImageBitmap(image)
    }

    override fun setCounter(counter: Int) {
        findViewById<TextView>(R.id.counter).run {
            show()
            text = if (counter > 99) "99+" else "$counter"
        }
    }

    override fun setBackground(background: Int) = Unit
}
