package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.database.dao.Participant
import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.MessageSender
import com.bendev.ssdb.utils.command.CommandContent
import com.bendev.ssdb.utils.command.Commands
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

class RegistrationContent private constructor(
    rawContent: String,
    val step: Participants.Step?,
    val content: String
) : CommandContent(rawContent) {

    override fun isWellFormatted(): Boolean {
        return step != null
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        super.onPrivateMessageReceived(event)

        val step = this.step ?: Participants.Step.NONE

        // get current participant or return if null
        val participant = SecretSantaDatabase.transactionDao {
            Participant.find {
                Participants.discordId eq event.channel.user.id
            }.elementAtOrNull(0)
        } ?: return

        // check if current step (in the command) is repeatable OR
        if (participant.registrationStep?.getNext() != step
                && !step.isRepeatable) {
            MessageSender.sendMessage(
                    event.channel,
                    "registration_error_bad_step",
                    Commands.CommandName.REGISTRATION.getFullCommand(),
                    participant.registrationStep?.getNext()?.name?.toLowerCase() ?: ""
            ) { /*nothing*/ }
            return
        }

        when (step) {
            Participants.Step.START -> {
                // Send private message to inform user on Start Step
                MessageSender.sendMessage(
                        event.channel,
                        "registration_start_message",
                        Commands.CommandName.REGISTRATION.getFullCommand(),
                        Participants.Step.LETTER.name.toLowerCase()
                ) {
                    // Save new step (the next one) if message has been sent
                    SecretSantaDatabase.transactionDao {
                        participant.registrationStep = Participants.Step.START
                    }
                }
            }

            Participants.Step.LETTER -> {
                // save the content as letter and save new step
                SecretSantaDatabase.transactionDao {
                    participant.secretLetter = content
                    participant.registrationStep = Participants.Step.LETTER
                }
                // inform user that it's able to resend and save a new content or finish
                MessageSender.sendMessage(
                        event.channel,
                        "registration_letter_message",
                        participant.secretLetter,
                        Commands.CommandName.REGISTRATION.getFullCommand(),
                        Participants.Step.FINISH.name.toLowerCase(),
                        Participants.Step.LETTER.name.toLowerCase()
                ) { /*nothing*/ }
            }

            Participants.Step.FINISH -> {
                // save finish step user cannot change its content anymore
                SecretSantaDatabase.transactionDao {
                    participant.registrationStep = Participants.Step.FINISH
                }
                MessageSender.sendMessage(
                        event.channel,
                        "registration_finish_message"
                ) { /*nothing*/ }
            }

            else -> {
                MessageSender.sendMessage(
                        event.channel,
                        "registration_error_no_step_found"
                ) { /*nothing*/ }
            }
        }

    }

    class RegistrationContentFactory {

        companion object {
            fun create(rawContent: String): CommandContent {
                val stepString: String = rawContent.trim().split(" ")[0]
                val content = rawContent.removePrefix(stepString).trim()

                val step = Participants.Step.values().find { it.name.equals(stepString, true) }

                return RegistrationContent(rawContent, step, content)
            }
        }

    }

}