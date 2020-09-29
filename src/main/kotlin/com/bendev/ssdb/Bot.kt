package com.bendev.ssdb

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.IOException
import javax.security.auth.login.LoginException

class Bot {

    @Throws(LoginException::class, IOException::class)
    fun init() {

        JDABuilder
            .create(
                "",
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS
            ).addEventListeners(
            ).build()

    }

}

@Throws(LoginException::class, IOException::class)
fun main(args: Array<String>) {

    Bot().init()

}