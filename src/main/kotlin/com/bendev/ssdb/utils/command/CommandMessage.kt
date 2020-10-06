package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.Constant
import net.dv8tion.jda.api.entities.Message

open class CommandMessage(
        val commandName: Commands.CommandName,
        val content: CommandContent) {



    companion object {

        fun parseCommandFromMessage(message: Message): CommandMessage? {

            val trimedMessage = message.contentDisplay.trim()
            val splittedMessage = trimedMessage.split(' ')
            val prefixedCommand = splittedMessage[0]
            val rawContent = trimedMessage.replace(prefixedCommand, "").trim()

            prefixedCommand.findAnyOf(
                    strings = Commands.CommandName.getPrefixedCommandList(),
                    startIndex = 0,
                    ignoreCase = true
            ) ?: return null

            val command = Commands.CommandName.values()
                    .find { it.value.contains(prefixedCommand.removePrefix(Constant.PREFIX)) } ?: return null

            val content = when (command) {
                Commands.CommandName.COMMAND -> EmptyContent()
                else -> EmptyContent()
            }

            if (!content.isWellFormatted()) return null

            return CommandMessage(command, content)

        }

    }

}