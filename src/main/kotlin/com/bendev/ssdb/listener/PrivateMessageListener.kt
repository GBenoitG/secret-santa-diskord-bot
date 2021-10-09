package com.bendev.ssdb.listener

import com.bendev.ssdb.utils.command.CommandMessage
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PrivateMessageListener : ListenerAdapter() {

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        super.onPrivateMessageReceived(event)

        if (event.author.isBot) return

        val jda = event.jda
        val commandMessage = CommandMessage.parseCommandFromMessage(event.message) ?: return

        commandMessage.content.onPrivateMessageReceived(event)

    }

}