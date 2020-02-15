package com.lounah.vkmc.core.recycler.base.items

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerHolderClickListener
import kotlinx.android.synthetic.main.item_error.*

data class ErrorView(
    @StringRes val title: Int? = null,
    @StringRes val subTitle: Int = R.string.try_one_more_time,
    val needRepeatButton: Boolean = true
) : ViewTyped {
    override val uid: String = "ERROR_VIEW"
    override val viewType: Int = R.layout.item_error
}

class ErrorViewHolder(
    view: View,
    repeatClickListener: RecyclerHolderClickListener
) : BaseViewHolder<ErrorView>(view) {

    init {
        repeatClickListener.accept(btnRepeat, this@ErrorViewHolder)
    }

    override fun bind(item: ErrorView) {
        item.title?.let(textWithIcon::setText)
        subText.setText(item.subTitle)
        btnRepeat.visibility = if (item.needRepeatButton) VISIBLE else GONE
    }
}
