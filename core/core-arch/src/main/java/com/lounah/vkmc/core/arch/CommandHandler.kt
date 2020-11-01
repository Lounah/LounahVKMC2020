package com.lounah.vkmc.core.arch

import io.reactivex.Observable

interface CommandsHandler<Command : Any, out Event : Any> {

    fun handle(commands: Observable<Command>): Observable<out Event>
}

// TODO: use `fun interface` for SAM
@Suppress("FunctionNaming")
inline fun <Command : Any, Event : Any> CommandsHandler(
    crossinline handle: (commands: Observable<Command>) -> Observable<out Event>
) = object : CommandsHandler<Command, Event> {
    override fun handle(commands: Observable<Command>) = handle(commands)
}
