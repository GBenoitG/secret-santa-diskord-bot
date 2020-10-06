package com.bendev.ssdb.utils.command.content

import com.bendev.ssdb.utils.Constant
import com.bendev.ssdb.utils.command.CommandContent

class InvitationContent(rawContent: String) : CommandContent(rawContent) {

    val description: String
    val reactions: List<String>

    init {

        val description = rawContent.split(":")[0].trim()
        val regex = Constant.REGEX_ALL_EMOJIS
        this.reactions = regex.findAll(rawContent).run {
            val list = MutableList(count()) { "" }
            forEachIndexed { index, item ->
                list[index] = item.value
            }
            list
        }

        this.description = description

    }

    override fun isWellFormatted(): Boolean = description.isNotBlank() && reactions.size == 3
    
}