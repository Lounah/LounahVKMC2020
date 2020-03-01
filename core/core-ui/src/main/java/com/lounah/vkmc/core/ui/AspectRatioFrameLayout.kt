package com.lounah.vkmc.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.widget.FrameLayout

private const val DEFAULT_ASPECT_RATIO = 1f

class AspectRatioFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs) {

    private var aspectRatio: Float = DEFAULT_ASPECT_RATIO

    init {
        attributeSet?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.AspectRatioFrameLayout, 0, 0)

            aspectRatio =
                typedArray.getFloat(
                    R.styleable.AspectRatioFrameLayout_aspfl_aspect_ratio,
                    DEFAULT_ASPECT_RATIO
                )

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val heightReconciledSpec =
            makeMeasureSpec(((getSize(widthSpec)) * aspectRatio).toInt(), EXACTLY)
        super.onMeasure(widthSpec, heightReconciledSpec)
    }
}
