package com.lounah.vkmc.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import androidx.cardview.widget.CardView

private const val DEFAULT_ASPECT_RATIO = 1f

class AspectRatioCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : CardView(context, attributeSet, defStyleAttrs) {

    private var aspectRatio: Float = DEFAULT_ASPECT_RATIO

    init {
        attributeSet?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.AspectRatioCardView, 0, 0)

            aspectRatio =
                typedArray.getFloat(
                    R.styleable.AspectRatioCardView_aspect_ratio,
                    DEFAULT_ASPECT_RATIO
                )

            typedArray.recycle()
        }

        cardElevation = 0f
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val heightReconciledSpec =
            makeMeasureSpec((getSize(widthSpec) * aspectRatio).toInt(), EXACTLY)
        super.onMeasure(widthSpec, heightReconciledSpec)
    }
}
