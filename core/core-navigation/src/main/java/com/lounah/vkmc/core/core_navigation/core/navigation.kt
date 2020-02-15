package com.lounah.vkmc.core.core_navigation.core

import androidx.collection.SparseArrayCompat
import androidx.fragment.app.FragmentManager
import java.util.*

typealias RouterResults = SparseArrayCompat<Stack<ResultPublisher<Any>>>
typealias DelayedResult = Pair<Int, Any>

data class Seed(val fm: FragmentManager, val containerId: Int)

interface RouterProvider {
    val router: Router
}

interface Navigator : RouterProvider {
    fun setRouter(router: Router)
    fun clearRouter()
}

interface Command {
    operator fun invoke(containerId: Int, fragmentManager: FragmentManager)
}

interface CommandExecutor {
    /** Seed represents an object, on which we perform all transactions. */
    val roots: Stack<Seed>
    val routerResults: RouterResults
    val delayedResults: Stack<DelayedResult>

    fun execute(command: Command)
}

interface ResultPublisher<T> {
    fun onResult(value: T)
}
