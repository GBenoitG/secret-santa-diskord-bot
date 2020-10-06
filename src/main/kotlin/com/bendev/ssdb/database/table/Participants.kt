package com.bendev.ssdb.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Participants : LongIdTable(name = "participants") {
    val discordId: Column<String> = varchar("discord_id", 32).index()
    val nickname: Column<String> = varchar("nickname", 32).default("")
}