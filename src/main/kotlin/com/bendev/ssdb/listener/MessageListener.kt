package com.bendev.ssdb.listener

import com.bendev.ssdb.utils.command.CommandMessage
import com.bendev.ssdb.utils.command.Commands
import com.bendev.ssdb.utils.command.content.InvitationContent
import com.bendev.ssdb.utils.properties.PropertiesManager
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.Exception

class MessageListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)

        if (event.author.isBot || event.isFromType(ChannelType.PRIVATE)) return

        val jda = event.jda
        val commandMessage = CommandMessage.parseCommandFromMessage(event.message) ?: return

        if (!PropertiesManager.isUserAllowed(event.guild, event.author)) {
            event.message.addReaction("❌").queue()
            return
        }

        commandMessage.content.onMessageReceived(event)

    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        super.onMessageUpdate(event)
    }

}