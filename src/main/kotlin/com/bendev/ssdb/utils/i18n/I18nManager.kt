package com.bendev.ssdb.utils.i18n

import java.text.MessageFormat
import java.util.*

object I18nManager {

    private lateinit var _messageStrings: ResourceBundle
    val messageStrings: ResourceBundle
        get() = _messageStrings

    fun initWithLocale(locale: Locale) {
        _messageStrings = ResourceBundle.getBundle("messages", locale, UTF8Control())
    }

    fun ResourceBundle.getFormattedString(key: String, vararg arguments: Any?): String {
        return MessageFormat.format(this.getString(key), *arguments)
    }

}