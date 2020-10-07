package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.command.content.InvitationContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CommandContentTest {


    @Test
    fun givenGoodRawMessage_initInvitationContent_shouldReturnGoodInvitationCommand() {

        // Given raw message
        val rawMessage = FakeMessage.FAKE_VALID_RAW_MESSAGE_INVITATION

        // Init InivitationContent
        val invitation = InvitationContent(rawMessage)

        // Assertion
        assertEquals(invitation.source, "This is a raw message well formatted :\n✅ I'm in\n❌ No I skip\n⏲ I think about it")
        assertEquals(invitation.description, "This is a raw message well formatted")

        assertEquals(invitation.reactions.size, 3)
        assertEquals(invitation.reactions, listOf("✅", "❌", "⏲"))

    }

}