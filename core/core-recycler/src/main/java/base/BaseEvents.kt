package com.lounah.vkmc.core.recycler.base

import androidx.lifecycle.LifecycleOwner
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface ReduxAction

abstract class BaseEvents<T : ReduxAction> {
    abstract val input: Consumer<T>
    abstract fun bindInternal(): Observable<T>

    open fun bind(lifecycle: LifecycleOwner) {
        bindInternal()
            .subscribe(input)
            .disposeOnDestroy(lifecycle)
    }
}
