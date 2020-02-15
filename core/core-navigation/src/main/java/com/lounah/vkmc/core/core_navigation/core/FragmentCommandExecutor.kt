package com.lounah.vkmc.core.core_navigation.core

import androidx.collection.SparseArrayCompat
import androidx.fragment.app.FragmentManager
import java.util.*

class FragmentCommandExecutor(container: Int, fm: FragmentManager) : CommandExecutor {

    override val routerResults: RouterResults = SparseArrayCompat()
    override val roots: Stack<Seed> = Stack()
    override val delayedResults: Stack<DelayedResult> = Stack()

    private val fragmentManager: FragmentManager
        get() = roots.peek().fm
    private val containerId: Int
        get() = roots.peek().containerId

    init {
        roots.push(Seed(fm, container))
    }

    override fun execute(command: Command) = command(containerId, fragmentManager)
}
