package com.lounah.vkmc.core.arch.impl

import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.arch.CommandsHandler
import com.lounah.vkmc.core.arch.Next
import com.lounah.vkmc.core.arch.RxStore
import com.lounah.vkmc.core.arch.Update
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

fun <State : Any, UiEvent : Event, News : Any, Event : Any, Command : Any> rxStore(
    initialState: State,
    initialCommands: List<Command> = emptyList(),
    commandsHandlers: List<CommandsHandler<Command, Event>> = emptyList(),
    newsPublisher: (Command) -> News?,
    update: Update<State, Event, Command> = Update { _, _ -> Next() }
): RxStore<State, UiEvent, News> = TeaRxStore(initialState, update, commandsHandlers, initialCommands, newsPublisher)

/**
 * Перегрузка [rxStore] для экранов, у которых [News] наследуется от [Command].
 *
 * @see [rxStore]
 */
inline fun <State : Any, UiEvent : Event, reified News : Command, Event : Any, Command : Any> rxStore(
    initialState: State,
    initialCommands: List<Command> = emptyList(),
    commandsHandlers: List<CommandsHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command> = Update { _, _ -> Next() }
): RxStore<State, UiEvent, News> {
    return rxStore(
        initialState = initialState,
        initialCommands = initialCommands,
        commandsHandlers = commandsHandlers,
        newsPublisher = { it as? News },
        update = update
    )
}

private class TeaRxStore<State : Any, UiEvent : Event, News : Any, Event : Any, Command : Any>(
    initialState: State,
    update: Update<State, Event, Command>,
    commandsHandlers: List<CommandsHandler<Command, Event>>,
    initialCommands: List<Command>,
    private val newsPublisher: (Command) -> News?,
    disposable: CompositeDisposable = CompositeDisposable()
) : RxStore<State, UiEvent, News>, Disposable by disposable {

    @Volatile
    override var currentState: State = initialState
        private set

    private val stateRelay = PublishRelay.create<State>()

    // toSerialized нужен для гарантии последовательной обработки,
    // если несколько CommandHandler'ов будут слать события одновременно с разных потоков
    private val eventsRelay = PublishRelay.create<Event>().toSerialized()

    // CachedRelay для гарантии обработки независимо от момента подписки
    private val newsRelay = PublishRelay.create<News>()

    init {
        val commandsRelay = PublishRelay.create<Command>()

        fun handleCommands(commands: List<Command>) {
            for (command in commands) {
                commandsRelay.accept(command)
                publishNews(command)
            }
        }

        eventsRelay.subscribe { event ->
            val next = update.update(currentState, event)

            val state = next.state
            if (state != null) {
                currentState = state
                stateRelay.accept(state)
            }

            handleCommands(next.commands)
        }.addTo(disposable)

        for (commandHandler in commandsHandlers) {
            commandHandler.handle(commandsRelay)
                .subscribe(eventsRelay::accept)
                .addTo(disposable)
        }

        handleCommands(initialCommands)
    }

    override fun dispatch(event: UiEvent) = eventsRelay.accept(event)

    override val state: Observable<State> = Observable.fromCallable(::currentState)
        .concatWith(stateRelay)
        .distinctUntilChanged()

    override val news: Observable<News> = newsRelay

    private fun publishNews(command: Command) {
        val news = newsPublisher(command)
        if (news != null) {
            newsRelay.accept(news)
        }
    }
}
