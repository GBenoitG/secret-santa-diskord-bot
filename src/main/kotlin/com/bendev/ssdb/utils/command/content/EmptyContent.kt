package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.utils.command.CommandContent

class EmptyContent : CommandContent("") {

    override fun isWellFormatted(): Boolean = true
}