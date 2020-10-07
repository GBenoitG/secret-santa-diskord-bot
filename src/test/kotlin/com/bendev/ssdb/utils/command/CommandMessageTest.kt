package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.command.content.InvitationContent
import io.mockk.every
import io.mockk.mockk
import net.dv8tion.jda.api.entities.Message
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class CommandMessageTest {

    @Test
    fun givenEmptyInvitationCommand_thenParseCommand_returnCommandNull() {

        // Prepare mock
        val message = mockk<Message>()
        every { message.contentDisplay } returns "ssdb!invitation"

        // Exec tested function
        val command = CommandMessage.parseCommandFromMessage(message)

        // assertion
        assertTrue(command == null)

    }

    @Test
    fun givenTypingErrorInvitationCommand_thenParseCommand_returnNull() {

        // Prepare mock
        val message = mockk<Message>()
        every { message.contentDisplay } returns "ssdb!invitatio ${FakeMessage.FAKE_VALID_RAW_MESSAGE_INVITATION}"

        // Exec tested function
        val command = CommandMessage.parseCommandFromMessage(message)

        // assertion
        assertTrue(command == null)

    }

    @Test
    fun givenValidInvitationCommand_thenParseCommand_returnInvitationCommand() {

        // Prepare mock
        val message = mockk<Message>()
        every { message.contentDisplay } returns "ssdb!invitation ${FakeMessage.FAKE_VALID_RAW_MESSAGE_INVITATION}"

        // Exec tested function
        val command = CommandMessage.parseCommandFromMessage(message)

        // assertion
        assertTrue(command != null)
        assertEquals(command!!.commandName, Commands.CommandName.INVITATION)
        assertEquals(command.content is InvitationContent, true)

    }

}