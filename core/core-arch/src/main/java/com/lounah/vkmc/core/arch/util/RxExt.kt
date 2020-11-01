package com.lounah.vkmc.core.arch.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.subscribeBy

private val onErrorStub: (Throwable) -> Unit = { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }

/**
 * Subscribes to this [Observable] on the main thread when [lifecycle] enters specified state and disposes it at exit.
 */
fun <T : Any> Observable<T>.subscribeWhen(
    lifecycle: Lifecycle,
    inState: Lifecycle.State,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) {
    var disposable: Disposable? = null
    lifecycle.observeStateBoundary(
        inState,
        onEnter = {
            disposable = observeOn(AndroidSchedulers.mainThread()).subscribeBy(onError, onComplete, onNext)
        },
        onExit = {
            disposable?.dispose()
            disposable = null
        }
    )
}

fun Completable.subscribeWhen(
    lifecycle: Lifecycle,
    inState: Lifecycle.State,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = {}
) {
    var disposable: Disposable? = null
    lifecycle.observeStateBoundary(
        inState,
        onEnter = {
            disposable = observeOn(AndroidSchedulers.mainThread()).subscribeBy(onError, onComplete)
        },
        onExit = {
            disposable?.dispose()
            disposable = null
        }
    )
}

private fun Lifecycle.observeStateBoundary(
    targetState: Lifecycle.State,
    onEnter: () -> Unit,
    onExit: () -> Unit
) {
    check(targetState != DESTROYED) { "Can't observe a boundary of this state" }

    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when {
                event == ON_CREATE && targetState == CREATED -> onEnter()
                event == ON_START && targetState == STARTED -> onEnter()
                event == ON_RESUME && targetState == RESUMED -> onEnter()
                event == ON_PAUSE && targetState == RESUMED -> onExit()
                event == ON_STOP && targetState == STARTED -> onExit()
                event == ON_DESTROY && (targetState == CREATED || targetState == INITIALIZED) -> onExit()
            }
        }
    })
}
