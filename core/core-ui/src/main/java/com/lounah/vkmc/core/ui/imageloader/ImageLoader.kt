package com.lounah.vkmc.core.ui.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.load(
    url: String,
    requestOptions: RequestOptions? = null,
    success: () -> Unit = {},
    failed: () -> Unit = {}
) = GlideApp.with(this)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade())
    .also { request -> requestOptions?.let(request::apply) }
    .setListener(failed, success)
    .into(this)

fun RequestBuilder<Drawable>.setListener(
    failed: () -> Unit,
    success: () -> Unit
): RequestBuilder<Drawable> = listener(object : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        failed()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        success()
        return false
    }
})
