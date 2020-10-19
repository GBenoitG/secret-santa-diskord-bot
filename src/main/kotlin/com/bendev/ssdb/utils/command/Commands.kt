package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.command.content.EmptyContent
import com.bendev.ssdb.utils.command.content.RegistrationContent
import org.jetbrains.annotations.NotNull
import kotlin.reflect.KClass

object Commands {

    enum class CommandName(val contentType: KClass<out CommandContent>, @NotNull vararg val value: String){
        /**
         * Help command display a list of help
         * */
        INVITATION(CommandContent::class, "invitation"),
        REGISTRATION(RegistrationContent::class, "registration");

        fun getFullCommand(): String {
            return "${Constant.PREFIX}${value[0]}"
        }

        companion object {
            fun getPrefixedCommandList() : List<String> = values().map {
                it.value.map { string ->
                    "${Constant.PREFIX}$string"
                }
            }.flatten()

        }

    }

}