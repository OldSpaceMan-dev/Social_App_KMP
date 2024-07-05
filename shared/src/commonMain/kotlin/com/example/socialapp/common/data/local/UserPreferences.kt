package com.example.socialapp.common.data.local


//константа для имени файла настроек
internal const val PREFERENCES_FILE_NAME = "app_user_settings.preferences_pb"


// общий интерфейс для чтения/записи в хранилище данных
internal interface UserPreferences {

    //извлечение данных
    suspend fun getUserData(): UserSettings

    //установка данных - принимают польз настройки для записи в хранилище данных
    suspend fun setUserData(userSettings: UserSettings)

}

// тк айос не может использовать найстроки сер/дессир данных андройд мы создали данный тип абстракции