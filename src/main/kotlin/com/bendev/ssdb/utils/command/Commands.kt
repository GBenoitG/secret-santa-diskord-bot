package com.bendev.ssdb.utils.command

import com.bendev.ssdb.utils.Constant
import org.jetbrains.annotations.NotNull

object Commands {

    enum class CommandName(val isAdminOnly: Boolean, @NotNull vararg val value: String){
        /**
         * Help command display a list of help
         * */
        INVITATION(true, "invitation"),
        REGISTRATION(false, "registration"),
        SHUFFLE(true, "shuffle");

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