package com.bendev.ssdb.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Participants : LongIdTable(name = "participants") {
    val discordId: Column<String> = varchar("discord_id", 32).index()
    val nickname: Column<String> = varchar("nickname", 32).default("")
    val registrationStep: Column<Step?> = enumeration("registration_step", Step::class).nullable()
    val secretLetter: Column<String> = text("secret_letter").default("")

    enum class Step {
        NONE,
        START,
        LETTER,
        FINISH;

        fun getNext(): Step? {
            return if (ordinal == values().size-1)
                null
            else values()[ordinal+1]
        }

    }

}