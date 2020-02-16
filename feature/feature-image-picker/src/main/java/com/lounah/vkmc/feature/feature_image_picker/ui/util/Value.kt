package com.lounah.vkmc.feature.feature_image_picker.ui.util

interface Value<T> {
    fun forceGet(): T = get() ?: error("Value is null")
    fun get(): T?
    fun set(value: T?)
}

class MemoryValueImpl<T>(
    private var internalValue: T? = null
) : Value<T> {

    override fun get(): T? = internalValue

    override fun set(value: T?) {
        internalValue = value
    }
}