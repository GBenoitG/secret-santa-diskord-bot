package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.database.dao.Participant
import com.bendev.ssdb.database.table.Participants
import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.MessageSender
import com.bendev.ssdb.utils.command.CommandContent
import com.bendev.ssdb.utils.command.Commands
import com.bendev.ssdb.utils.i18n.I18nManager
import com.bendev.ssdb.utils.properties.PropertiesManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent

class InvitationContent private constructor(
    rawContent: String,
    val description: String,
    val reactions: List<Pair<String, ReactionType>>
): CommandContent(rawContent) {

    override fun isWellFormatted(): Boolean = description.isNotBlank() && reactions.size == 3

    override fun onGenericMessageReaction(event: GenericMessageReactionEvent) {
        super.onGenericMessageReaction(event)
        val reactionType = reactions.find {
            it.first == event.reactionEmote.emoji
        }?.second

        // Auto delete bad reactions from users
        handleBadReaction(event, reactionType)

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

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)
        reactions.forEach { emoji ->
            if (emoji.first.isEmpty()) throw Exception("Emote is empty")
            event.message.addReaction(emoji.first).queue()
        }
        event.message.pin().queue()
        createRole(event.guild)

    }

    private fun positiveAction(event: GenericMessageReactionEvent) {
            when (event) {
                // Add reaction event: create or find user already created before
                is MessageReactionAddEvent -> {
                    val participant = SecretSantaDatabase.transactionDao {
                        Participant.find {
                            Participants.discordId eq event.userId
                        }.firstOrNull() ?: Participant.new {
                            discordId = event.userId
                            nickname = event.user?.name ?: ""
                        }
                    }
                    addOrRemoveSecretSantaRole(event.member, event.guild, true)
                    sendRegistrationMessage(participant, event)
                }

                is MessageReactionRemoveEvent -> {
                    val result = SecretSantaDatabase.transactionDao {
                        Participant.find {
                            Participants.discordId eq event.userId
                        }.elementAtOrNull(0)
                    } ?: return
                    if ((result.registrationStep?.ordinal ?: 0) >= Participants.Step.START.ordinal) {
                        event.jda.openPrivateChannelById(result.discordId).queue { private ->
                            MessageSender.sendAnswerableMessage(
                                    private,
                                    "invitation_warning_before_unsubscribe",
                                    arguments = emptyArray(),
                                    answers = arrayOf(
                                            MessageSender.Answers("✅", I18nManager.getCommonString("yes")) {
                                                SecretSantaDatabase.transactionDao {
                                                    result.delete()
                                                }
                                            },
                                            MessageSender.Answers("❌", I18nManager.getCommonString("no")) {
                                                MessageSender.sendMessage(
                                                        private,
                                                        "invitation_warning_pls_check_back") { /*nothing*/ }
                                            }
                                    )
                            )
                        }
                    }
                    addOrRemoveSecretSantaRole(event.member, event.guild, false)
                }
            }
    }

    private fun createRole(guild: Guild) {
        val roleName = PropertiesManager.properties.roleName
        if (!guild.roles.any { it.name == roleName }) {
            guild.createRole()
                .setName(roleName)
                .setMentionable(true)
                .queue()
        }
    }

    private fun addOrRemoveSecretSantaRole(member: Member?, guild: Guild, addRole: Boolean) {
        val roleName = PropertiesManager.properties.roleName
        val role = guild.roles.firstOrNull { it.name == roleName }
        member?.let {
            role?.let {
                if (addRole) {
                    guild.addRoleToMember(member, role).queue()
                } else {
                    guild.removeRoleFromMember(member, role).queue()
                }
            }
        }
    }

    private fun sendRegistrationMessage(participant: Participant, event: GenericMessageReactionEvent) {
        if (participant.registrationStep != null) return
        event.member?.apply {
            user.openPrivateChannel().queue {
                MessageSender.sendMessage(
                        it,
                        "registration_message_introduction",
                        Commands.CommandName.REGISTRATION.getFullCommand(),
                        Participants.Step.START.name.lowercase()
                ) {
                    SecretSantaDatabase.transactionDao {
                        participant.registrationStep = Participants.Step.NONE
                    }
                }
            }
        }
    }

    private fun standByAction(event: GenericMessageReactionEvent) {

    }

    private fun handleBadReaction(event: GenericMessageReactionEvent, reactionType: ReactionType?) {
        if (event is MessageReactionAddEvent) {
            // remove all bad reaction that is not match with expected ReactionType
            if (reactionType == null) {
                event.reaction.removeReaction(event.user!!).queue()
                return
            }

            val currentEmote = event.reactionEmote
            event.retrieveMessage().queue { message ->
                message.reactions.forEach {
                    if (it.reactionEmote != currentEmote)
                        it.removeReaction(event.user!!).queue()
                }

            }
        }
    }

    enum class ReactionType {
        POSITIVE,
        NEGATIVE,
        STANDBY
    }

    class InvitationContentFactory {

        companion object {
            fun create(rawContent: String): CommandContent {
                if (rawContent.isEmpty()) {
                    return InvalidContent("InvitationContent should not be empty")
                }
                val splitMessage = rawContent.split(":")
                val description = splitMessage[0].trim()
                val regex = Constant.REGEX_ALL_EMOJIS
                val reactions = regex.findAll(splitMessage[1].trim()).run {
                    val list = mutableListOf<Pair<String, ReactionType>>()
                    forEachIndexed { index, item ->
                        list.add(Pair(item.value, ReactionType.values()[index]))
                    }
                    list
                }

                return InvitationContent(rawContent, description, reactions)
            }
        }

    }
    
}