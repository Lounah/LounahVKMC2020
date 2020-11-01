package com.lounah.vkmc.core.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object ComponentStorage {

    val components = mutableMapOf<Class<Any>, Any>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> addComponent(component: T) {
        components[T::class.java as Class<Any>] = component
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> addComponents(components: List<T>) {
        components.forEach(::addComponent)
    }

    inline fun <reified T : Any> AppCompatActivity.getComponent(): T {
        return components.filterKeys { (it.isAssignableFrom(T::class.java)) }.values.first() as? T
            ?: error("Component ${T::class.java.simpleName} was not found")
    }


    inline fun <reified T : Any> Fragment.getComponent(): T {
        return components.filterKeys { (it.isAssignableFrom(T::class.java)) }.values.first() as? T
            ?: error("Component ${T::class.java.simpleName} was not found")
    }
}
