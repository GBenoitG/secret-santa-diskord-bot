package com.bendev.ssdb.database.dao

import com.bendev.ssdb.database.table.Participants
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Participant(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Participant>(Participants)
    var discordId by Participants.discordId
    var nickname by Participants.nickname
}