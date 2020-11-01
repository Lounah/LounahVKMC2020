package com.lounah.vkmc.core.arch

interface Update<State : Any, in Event : Any, out Command : Any> {

    fun update(state: State, event: Event): Next<State, Command>
}

// TODO: use `fun interface` for SAM
@Suppress("FunctionNaming")
inline fun <State : Any, Event : Any, Command : Any> Update(
    crossinline update: (state: State, event: Event) -> Next<State, Command>
) = object : Update<State, Event, Command> {
    override fun update(state: State, event: Event) = update(state, event)
}
