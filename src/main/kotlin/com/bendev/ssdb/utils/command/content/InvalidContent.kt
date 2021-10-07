package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.utils.command.CommandContent

class InvalidContent(
    val error: String
) : CommandContent("") {

    override fun isWellFormatted(): Boolean = false

}