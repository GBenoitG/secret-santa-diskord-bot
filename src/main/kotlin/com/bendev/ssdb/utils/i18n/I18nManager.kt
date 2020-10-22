package com.bendev.ssdb.utils.i18n

import java.text.MessageFormat
import java.util.*

object I18nManager {

    private lateinit var _messageStrings: ResourceBundle
    val messageStrings: ResourceBundle
        get() = _messageStrings
    private lateinit var _commonsStrings: ResourceBundle
    val commonsStrings: ResourceBundle
        get() = _commonsStrings

    fun initWithLocale(locale: Locale) {
        _messageStrings = ResourceBundle.getBundle("messages", locale, UTF8Control())
        _commonsStrings = ResourceBundle.getBundle("commons", locale, UTF8Control())
    }

    fun ResourceBundle.getFormattedString(key: String, vararg arguments: String?): String {
        return MessageFormat.format(this.getString(key), *arguments)
    }

    fun getCommonString(key: String, vararg arguments: String?): String {
        return MessageFormat.format(commonsStrings.getString(key), *arguments)
    }

}