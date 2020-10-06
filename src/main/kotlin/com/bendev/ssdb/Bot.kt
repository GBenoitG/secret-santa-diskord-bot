package com.bendev.ssdb

import com.bendev.ssdb.database.SecretSantaDatabase
import com.bendev.ssdb.listener.MessageListener
import com.bendev.ssdb.listener.ReactionListener
import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.properties.PropertiesManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.IOException
import javax.security.auth.login.LoginException

class Bot {

    @Throws(LoginException::class, IOException::class)
    fun init() {

        val properties = PropertiesManager.loadPropertiesFromFile(Constant.PROPERTIES_FILE_PATH)

        val jda = JDABuilder
            .create(
                properties.token,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS
            ).addEventListeners(
                MessageListener(),
                ReactionListener()
            ).build()

        jda.presence.activity = Activity.playing(properties.playingAt)

        SecretSantaDatabase.initDatabse()

    }

}

@Throws(LoginException::class, IOException::class)
fun main(args: Array<String>) {

    if (args.isNotEmpty()) {
        when (args[0]) {
            "init" -> PropertiesManager.initFile(Constant.PROPERTIES_FILE_PATH)
        }
        return
    }

    Bot().init()

}