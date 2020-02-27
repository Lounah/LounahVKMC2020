package com.lounah.vkmc.feature.feature_map.map.ui.map.markers

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Priority
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import com.lounah.vkmc.feature.feature_map.R

internal class GroupOrEventView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs),
    MarkerView {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_map_group_or_event, this, true)
    }

    override fun setIcon(path: String) {
        GlideApp.with(context).asBitmap().load(path).priority(Priority.IMMEDIATE)
            .circleCrop()
            .override(32.dp(context), 32.dp(context)).into(findViewById(R.id.icon))
    }

    override fun setCounter(counter: Int) {
        findViewById<TextView>(R.id.counter).text = if (counter > 9) "9+" else "$counter"
    }

    override fun setImage(image: Bitmap) {
        findViewById<ImageView>(R.id.icon).setImageBitmap(image)
    }

    override fun setBackground(bg: Int) {
        background = ContextCompat.getDrawable(context, bg)
    }
}
