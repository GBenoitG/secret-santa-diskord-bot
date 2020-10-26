package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.database.dao.Participant
import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.MessageSender
import com.bendev.ssdb.utils.command.CommandContent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.and

class ShuffleContent : CommandContent("") {

    override fun isWellFormatted(): Boolean {
        return true
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)

        shuffleAction(event)

    }

    private fun shuffleAction(event: MessageReceivedEvent) = synchronized(this) {

        val result = SecretSantaDatabase.transactionDao {
            // get all participant who are validate the conditions : at least Step.Letter with a secretLetter written
            val participantList = Participant.find {
                (Participants.registrationStep greaterEq Participants.Step.LETTER) and
                        (Participants.secretLetter greater "") and
                        Participants.secretSanta.isNull()
            }.map { it }

            if (participantList.size <= 1) return@transactionDao TransactionStatus(
                    Status.ERROR,
                    "error_shuffle_not_enough_participants")

            // shuffle participant list
            val newList = participantList.shuffled()

            newList.forEachIndexed { index, participant ->
                var reindex = index - 1
                reindex = if (reindex < 0) newList.size - 1 else reindex
                participant.secretSanta = newList[reindex]
            }
            TransactionStatus(Status.SUCCESS)
        }

        when (result.status) {
            Status.SUCCESS -> distributeLetter(event.jda)
            Status.ERROR -> sendError(event.author, result.messageErrorKey!!)
        }

    }

    private fun distributeLetter(jda: JDA) {

        // Get all secret letter and secretSanta
        val secretSantasList = SecretSantaDatabase.transactionDao {
            Participant
                    .find { Participants.secretSanta.isNotNull() }
                    .map { Pair(it.secretLetter, it.secretSanta) }
        }

        // send private message to each one with the letter
        secretSantasList.forEach {
            jda.openPrivateChannelById(it.second!!.discordId).queue { channel ->
                MessageSender.sendMessage(
                        channel,
                        "shuffle_distribution_letter",
                        it.second?.nickname ?: "",
                        it.first
                ) { /*nothing*/ }
            }
        }

    }

    private fun sendError(user: User, errorKey: String) {
        user.openPrivateChannel().queue { channel ->
            MessageSender.sendError(channel, errorKey) { /*nothing*/ }
        }
    }

    data class TransactionStatus(val status: Status, val messageErrorKey: String? = null)

    enum class Status {
        SUCCESS,
        ERROR
    }

}