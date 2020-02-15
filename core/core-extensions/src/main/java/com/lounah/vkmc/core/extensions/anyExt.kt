@file:Suppress("NOTHING_TO_INLINE")

package com.lounah.vkmc.core.extensions

@Suppress("UNCHECKED_CAST")
inline fun <T> Any?.asType(): T {
    return this as T
}

inline fun <T> Any?.getOrThrow(): T =
    this.asType() ?: error("`getOrThrow` returned null value on: $this")