package com.lounah.vkmc.core.arch.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import com.lounah.vkmc.core.arch.RxStore
import com.lounah.vkmc.core.arch.util.subscribeWhen
import io.reactivex.disposables.Disposable

/**
 * Привязывает жизненный цикл данного [RxStore] к указанному [lifecycleOwner].
 *
 * Подписка на `state` и `news` выполняется в `onStart`, отписка в `onStop`.
 * Все события перенаправляются на главный поток.
 *
 * Сам [RxStore] уничтожается при onDestroy.
 */
fun <State : Any, UiEvent : Any, News : Any> RxStore<State, UiEvent, News>.bind(
    lifecycleOwner: LifecycleOwner,
    render: (State) -> Unit,
    handleNews: (News) -> Unit
) {
    bind(lifecycleOwner.lifecycle, render, handleNews)
}

/**
 * Привязывает жизненный цикл данного [RxStore] к указанному [lifecycle].
 *
 * @see bind
 */
fun <State : Any, UiEvent : Any, News : Any> RxStore<State, UiEvent, News>.bind(
    lifecycle: Lifecycle,
    render: (State) -> Unit,
    handleNews: (News) -> Unit
) {
    state.subscribeWhen(lifecycle, STARTED, onNext = render)
    news.subscribeWhen(lifecycle, STARTED, onNext = handleNews)
    disposeOnDestroy(lifecycle)
}

fun Disposable.disposeOnDestroy(lifecycle: Lifecycle) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            dispose()
            lifecycle.removeObserver(this)
        }
    })
}
