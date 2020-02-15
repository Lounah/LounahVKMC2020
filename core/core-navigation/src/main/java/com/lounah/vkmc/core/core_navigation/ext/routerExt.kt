@file:Suppress("UNCHECKED_CAST")

package com.lounah.vkmc.core.core_navigation.ext

import com.lounah.vkmc.core.core_navigation.core.ResultPublisher
import com.lounah.vkmc.core.core_navigation.core.Router
import com.lounah.vkmc.core.core_navigation.core.RouterResults
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import java.util.*

inline fun <reified T> Router.onFragmentResultEvent(requestCode: Int): Observable<T> {
    return ResultObservable<T>(routerResults, requestCode).ofType(T::class.java)
}

class ResultObservable<T>(
    private val routerResults: RouterResults,
    private val requestCode: Int
) : Observable<T>() {

    override fun subscribeActual(observer: Observer<in T>) {
        val resultListener = ResultListener(routerResults, requestCode, observer)
        val newRequest = routerResults.get(requestCode) ?: Stack()
        newRequest.push(resultListener as ResultPublisher<Any>)
        routerResults.put(requestCode, newRequest)
        observer.onSubscribe(resultListener)
    }

    private class ResultListener<T>(
        private val routerResults: RouterResults,
        private val requestCode: Int,
        private val observer: Observer<in T>
    ) : ResultPublisher<T>,
        MainThreadDisposable() {

        override fun onResult(value: T) {
            if (!isDisposed) observer.onNext(value)
        }

        private fun popCurrent() {
            val listeners = routerResults.get(requestCode) ?: Stack()
            if (listeners.size <= 1) routerResults.remove(requestCode)
            else routerResults.put(requestCode, listeners.apply { removeAt(lastIndex) })
        }

        override fun onDispose() = popCurrent()
    }
}
