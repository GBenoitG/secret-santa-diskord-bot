package com.bendev.ssdb.utils

import com.bendev.ssdb.utils.i18n.I18nManager
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class I18nManagerTest {

    @BeforeEach
    fun init() {

        // unknown country set to the default locale
        Locale.setDefault(Locale.forLanguageTag("qwerty"))

    }

    @Test
    fun givenFrenchLocale_whenInitI18nManager_thenReturnFrenchResources() {

        // given French Locale
        val locale = Locale.FRENCH

        // when init I18nManager
        I18nManager.initWithLocale(locale)

        // then answer is testFR
        assertTrue(I18nManager.messageStrings.getString("test") == "testFR")

    }

    @Test
    fun givenEnglishLocale_whenInitI18nManager_thenReturnEnglishResources() {

        // given English Locale
        val locale = Locale.ENGLISH

        // when init I18nManager
        I18nManager.initWithLocale(locale)

        // then answer is testEN
        assertTrue(I18nManager.messageStrings.getString("test") == "testEN")

    }

    @Test
    fun givenDefaultLocale_whenInitI18nManager_thenReturnDefaultResources() {

        // given Default Locale
        val locale = Locale.getDefault()

        // when init I18nManager
        I18nManager.initWithLocale(locale)

        // then answer is testEN
        assertTrue(I18nManager.messageStrings.getString("test") == "testDefault")

    }

    @Test
    fun givenUnmanagedLocale_whenInitI18nManager_thenReturnDefaultResources() {

        // given unmanaged Locale
        val locale = Locale.TAIWAN

        // when init I18nManager
        I18nManager.initWithLocale(locale)

        // then answer is OK
        assertTrue(I18nManager.messageStrings.getString("test") == "testDefault")

    }

}