package com.bendev.ssdb.listener

import com.bendev.ssdb.utils.command.CommandMessage
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReactionListener : ListenerAdapter() {

    override fun onGenericMessageReaction(event: GenericMessageReactionEvent) {
        super.onGenericMessageReaction(event)

        event.channel.retrieveMessageById(event.messageId).queue { message ->
            val commandMessage = CommandMessage.parseCommandFromMessage(message) ?: return@queue

            commandMessage.content.onGenericMessageReaction(event)

        }

    }


}