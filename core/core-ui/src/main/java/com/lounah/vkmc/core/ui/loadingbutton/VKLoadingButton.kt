package com.lounah.vkmc.core.ui.loadingbutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.lounah.vkmc.core.extensions.hide
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.ui.R
import com.lounah.vkmc.core.ui.util.ClickLock
import com.lounah.vkmc.core.ui.util.throttledClick
import kotlinx.android.synthetic.main.button_vk_loading.view.*

class VKLoadingButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttrs) {

    private val clickLock = ClickLock()

    var text: CharSequence = ""
        set(value) {
            field = value
            actionButton.text = value
        }

    var loading: Boolean = false
        set(value) {
            field = value
            actionButton.isEnabled = !value
            if (value) {
                actionButton.text = ""
                progressBar.show()
            } else {
                progressBar.hide()
                actionButton.text = text
            }
        }

    var buttonSelected: Boolean = false
        set(value) {
            field = value
            actionButton.isSelected = field
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.button_vk_loading, this, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.uikit_white))
    }

    fun setButtonClickListener(listener: () -> Unit) {
        actionButton.throttledClick(clickLock) { listener() }
    }

    fun setButtonBackgroundColor(@ColorInt color: Int) {
        setBackgroundColor(color)
    }

    fun setButtonTextColor(color: ColorStateList) {
        actionButton.setTextColor(color)
    }

    fun setButtonBackground(drawable: Drawable) {
        actionButton.background = drawable
    }
}
