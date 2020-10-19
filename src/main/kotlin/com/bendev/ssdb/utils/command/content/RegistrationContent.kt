package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.database.dao.Participant
import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.I18nManager
import com.bendev.ssdb.utils.I18nManager.getFormattedString
import com.bendev.ssdb.utils.command.CommandContent
import com.bendev.ssdb.utils.command.Commands
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

class RegistrationContent(rawContent: String) : CommandContent(rawContent) {

    val step: Participants.Step?
    val content: String

    init {
        val stepString: String = rawContent.trim().split(" ")[0]
        val content = rawContent.removePrefix(stepString).trim()

        val step = Participants.Step.values().find { it.name.equals(stepString, true) }

        this.content = content
        this.step = step

    }

    override fun isWellFormatted(): Boolean {
        return step != null
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        super.onPrivateMessageReceived(event)

        val step = this.step ?: return

        // get current participant or return if null
        val participant = SecretSantaDatabase.transactionDao {
            Participant.find {
                Participants.discordId eq event.channel.user.id
            }.elementAtOrNull(0)
        } ?: return

        // check if current step (in the command) is repeatable OR
        if ((participant.registrationStep?.getNext() != step).also { print(it) }
                && !step.isRepeatable) {
            sendPrivateMessage(event,
                    "registration_error_bad_step",
                    Commands.CommandName.REGISTRATION.getFullCommand(),
                    participant.registrationStep?.getNext()?.name?.toLowerCase() ?: ""
            ) {/*nothing*/}
            return
        }

        when (step) {
            Participants.Step.START -> {
                // Send private message to inform user on Start Step
                sendPrivateMessage(event,
                        "registration_start_message",
                        Commands.CommandName.REGISTRATION.getFullCommand(),
                        Participants.Step.LETTER.name.toLowerCase()
                ) {
                    // Save new step (the next one) if message has been sent
                    SecretSantaDatabase.transactionDao {
                        participant.registrationStep = Participants.Step.LETTER
                    }
                }
            }

            Participants.Step.LETTER -> {
                SecretSantaDatabase.transactionDao {
                    participant.secretLetter = content
                }
                sendPrivateMessage(event,
                        "registration_letter_message",
                        participant.secretLetter,
                        Commands.CommandName.REGISTRATION.getFullCommand(),
                        Participants.Step.FINISH.name.toLowerCase(),
                        Participants.Step.LETTER.name.toLowerCase()
                ) {/*nothing*/ }
            }

            Participants.Step.FINISH -> {
                SecretSantaDatabase.transactionDao {
                    participant.registrationStep = Participants.Step.FINISH
                }
                sendPrivateMessage(
                        event,
                        "registration_finish_message"
                ) {/*nothing*/}
            }

            else -> {}
        }

    }

    private fun sendPrivateMessage(event: PrivateMessageReceivedEvent,
                                   key: String,
                                   vararg arguments: Any,
                                   action: (Message) -> Unit) {
        event.channel.sendMessage(
                I18nManager.messageStrings.getFormattedString(
                        key,
                        *arguments,
                )
        ).queue { action.invoke(it) }
    }
}