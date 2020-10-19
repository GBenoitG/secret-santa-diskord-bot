package com.bendev.ssdb.listener

import com.bendev.ssdb.utils.command.CommandMessage
import com.bendev.ssdb.utils.command.Commands
import com.bendev.ssdb.utils.command.content.InvitationContent
import com.bendev.ssdb.utils.properties.PropertiesManager
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.Exception

class PrivateMessageListener : ListenerAdapter() {

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        super.onPrivateMessageReceived(event)

        if (event.author.isBot) return

        val jda = event.jda
        val commandMessage = CommandMessage.parseCommandFromMessage(event.message) ?: return

        commandMessage.content.onPrivateMessageReceived(event)

    }

}