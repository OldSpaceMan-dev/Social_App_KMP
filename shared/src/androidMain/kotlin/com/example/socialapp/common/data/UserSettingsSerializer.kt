package com.example.socialapp.common.data

import androidx.datastore.core.Serializer
import com.example.socialapp.common.data.local.UserSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

// tell Proto data store how write/read our data // - UserSettings


object UserSettingsSerializer: Serializer<UserSettings>{

    override val defaultValue: UserSettings
        get() = UserSettings()//return default user settings


    //transform store data into representation or return user settings from
    //data that is being stored
    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {//new settings object, try if data can't be serialized
            //Десериализует заданную строку JSON в значение типа T
            Json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (serializationExc: SerializationException){
            defaultValue
        }
    }

    //where we transform our data (UserSettings) into format that fits(подходит)
    //for the storage
    //format that data store knows how to store
    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = UserSettings.serializer(),// сереализует в эквивалентый джейсон
                value = t //what we serialized
            ).encodeToByteArray()
        )
    }
}