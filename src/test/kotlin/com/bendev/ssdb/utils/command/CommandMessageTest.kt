package com.bendev.ssdb.utils.command

import io.mockk.every
import io.mockk.mockk
import net.dv8tion.jda.api.entities.Message
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CommandMessageTest {

    @Test
    fun parseCommand_callCommandCommand_returnCommandWithEmptyContent() {

        // Prepare mock
        val message = mockk<Message>()
        every { message.contentDisplay } returns "ssdb!command"

        // Exec tested function
        val command = CommandMessage.parseCommandFromMessage(message)

        // assertion
        assertEquals(command != null, true)
        assertEquals(command!!.commandName, Commands.CommandName.COMMAND)
        assertEquals(command.content is EmptyContent, true)

    }

}