package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.database.dao.Participant
import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.command.CommandContent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import java.lang.Exception

class InvitationContent(rawContent: String) : CommandContent(rawContent) {

    val description: String
    val reactions: List<Pair<String, ReactionType>>

    init {

        val description = rawContent.split(":")[0].trim()
        val regex = Constant.REGEX_ALL_EMOJIS
        this.reactions = regex.findAll(rawContent).run {
            val list = mutableListOf<Pair<String, ReactionType>>()
            forEachIndexed { index, item ->
                list.add(Pair(item.value, ReactionType.values()[index]))
            }
            list
        }

        this.description = description

    }

    override fun isWellFormatted(): Boolean = description.isNotBlank() && reactions.size == 3

    override fun onGenericMessageReaction(event: GenericMessageReactionEvent) {
        super.onGenericMessageReaction(event)
        val reactionType = reactions.find {
            it.first == event.reactionEmote.emoji
        }?.second

        // Auto delete bad reaction from users
        if (event is MessageReactionAddEvent && reactionType == null) {
            event.reaction.removeReaction(event.user!!).queue()
            return
        }

        when (reactionType) {
            ReactionType.POSITIVE -> {
                positiveAction(event)
            }

            ReactionType.STANDBY -> {
                standByAction(event)
            }

            else -> { } // NOTHING
        }
    }

    private fun positiveAction(event: GenericMessageReactionEvent) {
        SecretSantaDatabase.transactionDao {
            when (event) {
                is MessageReactionAddEvent -> Participant.new {
                    discordId = event.userId
                    nickname = event.user?.name ?: ""
                }
                is MessageReactionRemoveEvent -> {
                    val result = Participant.find {
                        Participants.discordId eq event.userId
                    }
                    if (result.empty()) return@transactionDao
                    result.first().delete()
                }
            }
        }
    }

    private fun standByAction(event: GenericMessageReactionEvent) {

    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)
        reactions.forEach { emoji ->
            if (emoji.first.isEmpty()) throw Exception("Emote is empty")
            event.message.addReaction(emoji.first).queue()
        }
    }

    enum class ReactionType {
        POSITIVE,
        NEGATIVE,
        STANDBY
    }
    
}