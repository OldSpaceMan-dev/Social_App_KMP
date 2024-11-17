package com.example.socialapp.android.common.util

import android.content.Context

//для удаления кеша // Очистка файлов в папке cache

class CacheManager(private val context: Context) {
    suspend fun clearCache() {
        val cacheDir = context.cacheDir // Получаем кэш приложения

        // Если директория существует, очищаем ее
        if (cacheDir.exists()) {
            cacheDir.listFiles()?.forEach { file ->
                file.deleteRecursively()
            }
        }

    }
}