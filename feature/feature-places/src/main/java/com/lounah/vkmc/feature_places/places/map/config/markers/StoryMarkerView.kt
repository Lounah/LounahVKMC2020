package com.lounah.vkmc.feature_places.places.map.config.markers

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.feature.feature_places.R

internal class StoryMarkerView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs), MarkerView {

    private val icon: ImageView
    private val counter: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_map_story, this, true)
        icon = findViewById(R.id.icon)
        counter = findViewById(R.id.counter)
    }

    override fun setCounter(counter: Int) {
        this.counter.run {
            show()
            text = if (counter > 99) "99+" else "$counter"
        }
    }
}
