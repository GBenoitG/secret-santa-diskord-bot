package com.bendev.ssdb.database

import com.bendev.ssdb.database.table.Participants
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SecretSantaDatabase private constructor(val database: Database){

    companion object {

        @Volatile
        private var INSTANCE: SecretSantaDatabase? = null

        fun initDatabse(): SecretSantaDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    val database = Database.connect(
                        url = "jdbc:sqlite:secret-santa-db.db",
                        driver = "org.sqlite.JDBC",
                        user = "root",
                        password = ""
                    )

                    transaction {
                        addLogger(StdOutSqlLogger)
                        SchemaUtils.create(Participants)
                    }

                    instance = SecretSantaDatabase(database)

                    INSTANCE = instance
                }
                return instance
            }
        }

        fun <T> transactionDao(action: Transaction.() -> T) = transaction {
            return@transaction action.invoke(this)
        }

    }
}