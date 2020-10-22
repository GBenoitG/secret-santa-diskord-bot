package com.bendev.ssdb.utils

import com.bendev.ssdb.listener.ReactionListener
import com.bendev.ssdb.utils.i18n.I18nManager
import com.bendev.ssdb.utils.i18n.I18nManager.getFormattedString
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

object MessageSender {

    fun sendMessage(channel: MessageChannel,
                    key: String,
                    vararg arguments: String,
                    onSuccess: (Message) -> Unit) {
        channel.sendMessage(
                I18nManager.messageStrings.getFormattedString(
                        key,
                        *arguments
                )
        ).queue {
            onSuccess.invoke(it)
        }
    }

    fun sendAnswerableMessage(channel: MessageChannel,
                              key: String,
                              vararg arguments: String?,
                              answers: Array<Answers>) {
        channel.sendMessage(
                I18nManager.messageStrings.getFormattedString(
                        key,
                        *arguments
                )
        ).queue { msg ->
            val stringAnswers = answers.joinToString("\n")
            msg.editMessage("${msg.contentRaw}\n${stringAnswers}").queue()
            answers.forEach {
                msg.addReaction(it.emoji).queue()
            }

            val listener = channel.jda.registeredListeners.find { it is ReactionListener } as ReactionListener
            listener.answerableMessageListener[msg.id] = { emote ->
                answers.find { it.emoji == emote }?.action?.invoke()
                msg.delete().queue()
            }

        }
    }

    data class Answers(val emoji: String, val name: String, val action: () -> Unit) {
        override fun toString(): String {
            return "$emoji $name"
        }
    }

}