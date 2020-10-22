package com.bendev.ssdb.listener

import com.bendev.ssdb.utils.command.CommandMessage
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReactionListener : ListenerAdapter() {

    var answerableMessageListener: MutableMap<String, (String) -> Unit> = hashMapOf()

    override fun onGenericMessageReaction(event: GenericMessageReactionEvent) {
        super.onGenericMessageReaction(event)

        event.channel.retrieveMessageById(event.messageId).queue { message ->
            val commandMessage = CommandMessage.parseCommandFromMessage(message) ?: return@queue

            commandMessage.content.onGenericMessageReaction(event)

        }

        if (event.user?.isBot == true) return
        answerableMessageListener[event.messageId]?.invoke(event.reactionEmote.emoji)

    }


}