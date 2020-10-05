package com.bendev.ssdb.utils

object Constant {

    const val PROPERTIES_FILE_PATH = "properties.json"

    const val PREFIX = "ssdb!"

    val REGEX_ALL_EMOJIS =
            """(\u00a9|\u00ae|[\u2000-\u3300]|\ud83c[\ud000-\udfff]|\ud83d[\ud000-\udfff]|\ud83e[\ud000-\udfff])"""
                    .toRegex(RegexOption.MULTILINE)

}