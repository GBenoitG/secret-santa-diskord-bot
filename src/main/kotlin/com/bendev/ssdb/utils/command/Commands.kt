package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.Constant
import org.jetbrains.annotations.NotNull
import kotlin.reflect.KClass

object Commands {

    enum class CommandName(val contentType: KClass<out CommandContent>, @NotNull vararg val value: String){
        /**
         * Help command display a list of help
         * */
        COMMAND(CommandContent::class, "command");

        fun getFullCommand(): String {
            return "${Constant.PREFIX}${this.value}"
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