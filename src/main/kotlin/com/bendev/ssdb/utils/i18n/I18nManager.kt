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
    private lateinit var _errorsStrings: ResourceBundle
    val errorsStrings: ResourceBundle
        get() = _errorsStrings

    fun initWithLocale(locale: Locale) {
        _messageStrings = ResourceBundle.getBundle("messages", locale, UTF8Control())
        _commonsStrings = ResourceBundle.getBundle("commons", locale, UTF8Control())
        _errorsStrings = ResourceBundle.getBundle("errors", locale, UTF8Control())
    }

    fun ResourceBundle.getFormattedString(key: String, vararg arguments: String?): String {
        return MessageFormat.format(this.getString(key), *arguments)
    }

    fun getCommonString(key: String, vararg arguments: String?): String {
        return MessageFormat.format(commonsStrings.getString(key), *arguments)
    }

    fun getErrorString(key: String, vararg arguments: String?): String {
        return MessageFormat.format(errorsStrings.getString(key), *arguments)
    }

}