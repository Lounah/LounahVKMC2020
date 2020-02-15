package com.lounah.vkmc.core.core_navigation.core

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.lounah.vkmc.core.core_navigation.commands.*


class Router(private val executor: CommandExecutor) : CommandExecutor by executor {

    fun root(fragment: Fragment) = execute(RootCommand(fragment, executor.roots))

    fun go(fragment: Fragment,
           onTransaction: FragmentTransaction.() -> FragmentTransaction = { this },
           backStack: Boolean = true,
           needResult: Boolean = true) =
        execute(ForwardCommand(fragment, onTransaction, backStack, needResult))

    fun showDialog(fragment: DialogFragment) = execute(ShowDialogCommand(fragment))

    fun back(backStackIsEmptyAction: () -> Unit = {}) {
        val hasDelayedResults = executor.delayedResults.isNotEmpty()
        val back = BackCommand(executor.roots, hasDelayedResults, backStackIsEmptyAction) {
            val (requestCode, result) = executor.delayedResults.pop()
            backWithResult(requestCode, result)
        }
        execute(back)
    }

    fun backWithResult(requestCode: Int, result: Any = Unit) {
        val backDelegate = BackCommand(executor.roots, false, {}, {})
        execute(BackWithResultCommand(requestCode, result, backDelegate, executor.routerResults))
    }

    fun postResult(requestCode: Int, result: Any = Unit) {
        execute(PostResultCommand(requestCode, result, executor.delayedResults))
    }

    fun switchActivity(target: AppCompatActivity,
                       @IdRes
                       containerId: Int, fragment: Fragment? = null) {
        val rootCommandDelegate = fragment?.let { RootCommand(it, executor.roots) } ?: EmptyCommand
        val switchActivity = SwitchActivityCommand(target,
            containerId,
            executor.roots,
            rootCommandDelegate)
        execute(switchActivity)
    }
}
