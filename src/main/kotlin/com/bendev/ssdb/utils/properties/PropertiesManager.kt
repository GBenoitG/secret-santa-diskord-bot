package com.bendev.ssdb.utils.properties

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

object PropertiesManager {

    private lateinit var filePath: String
    lateinit var properties: Properties
        private set

    private val json: Json by lazy { Json {
        prettyPrint = true
    } }

    @Synchronized
    fun loadPropertiesFromFile(propertiesFilePath: String) : Properties {

        val file = initFile(propertiesFilePath)
        if (file.readBytes().toString(Charsets.UTF_8).isEmpty()) initPropertiesFile(file)

        file.inputStream().use {
            val data = it.readBytes().toString(Charsets.UTF_8)
            properties = json.decodeFromString(data)
        }
        filePath = propertiesFilePath
        return properties
    }

    fun updateProperties(newProperties: Properties) {

        val file = File(filePath)

        if (!file.exists()) file.delete()

        file.createNewFile()

        val dataToWrite = json.encodeToString(Properties.serializer(), newProperties)

        file.writer(Charsets.UTF_8).use {
            it.write(dataToWrite)
        }

    }

    private fun createPropertiesFile(file: File) {
        file.createNewFile()
        initPropertiesFile(file)
    }

    private fun initPropertiesFile(file: File) {
        val defaultProperties = Properties(
            token = "",
        playingAt = "")

        val dataToWrite = json.encodeToString(Properties.serializer(), defaultProperties)
        file.outputStream().use {
            it.write(dataToWrite.toByteArray(Charsets.UTF_8))
        }
    }

    fun initFile(propertiesFilePath: String): File {

        if (propertiesFilePath.isEmpty()) throw Exception("You must provide a correct properties file path")

        val file = File(propertiesFilePath)

        if (!file.exists()) createPropertiesFile(file)

        return file
    }

}