package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.command.FakeMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RegistrationContentTest {


    @Test
    fun givenRegistrationStartStep_initRegistrationContent_shouldReturnGoodRegistrationContent() {

        // Given raw message
        val rawMessage = FakeMessage.FAKE_VALID_RAW_MESSAGE_START_REGISTRATION

        // Init RegistrationContent
        val registration = RegistrationContent(rawMessage)

        // Assertion
        assertEquals(Participants.Step.START, registration.step)
        assertEquals("", registration.content)

    }

    @Test
    fun givenUnknownRegistrationStep_initRegistrationContent_shouldReturnNullStep() {

        // Given unknown registration option (like typing error)
        val rawMessage = "star"

        // Init RegistrationContent
        val registration = RegistrationContent(rawMessage)

        // Assertion
        assertNull(registration.step)
        assertEquals("", registration.content)

    }

    @Test
    fun givenGoodInit_whenIsWellFormattedCalled_shouldReturnTrue() {

        // Given valid registration content
        val registrationContent = RegistrationContent(FakeMessage.FAKE_VALID_RAW_MESSAGE_START_REGISTRATION)

        // When isWellFormatted called
        val result = registrationContent.isWellFormatted()

        assertTrue(result)

    }

    @Test
    fun givenBadInit_whenIsWellFormattedCalled_shouldReturnFalse() {

        // Given bad registration content (like typing error)
        val registrationContent = RegistrationContent("star")

        // When isWellFormatted called
        val result = registrationContent.isWellFormatted()

        assertFalse(result)

    }

}