package com.bendev.ssdb.utils

import java.util.*

object I18nManager {

    private lateinit var _messageStrings: ResourceBundle
    val messageStrings: ResourceBundle
        get() = _messageStrings

    fun initWithLocale(locale: Locale) {
        _messageStrings = ResourceBundle.getBundle("messages", locale)
    }

}