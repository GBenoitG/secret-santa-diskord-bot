package com.bendev.ssdb.utils.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent

abstract class CommandContent protected constructor(
    val source: String
) {

    /**
     * Check if the content is well formatted
     *
     * @return true - if the content is well formatted
     * */
    abstract fun isWellFormatted(): Boolean

    /**
     *
     * */
    open fun onGenericMessageReaction(event: GenericMessageReactionEvent) {}

    /**
     *
     * */
    open fun onMessageReceived(event: MessageReceivedEvent) {}

    /**
     *
     * */
    open fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {}
}