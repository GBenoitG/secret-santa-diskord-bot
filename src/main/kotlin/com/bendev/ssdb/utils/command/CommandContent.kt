package com.bendev.ssdb.utils.command

abstract class CommandContent(
        val source: String
) {
    abstract fun isWellFormatted(): Boolean
}