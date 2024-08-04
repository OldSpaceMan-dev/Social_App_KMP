package com.example.socialapp.android.common.util

private const val CURRENT_BASE_URL = "http://192.168.1.138:8080/"


fun String.toCurrentUrl(): String {
    return if (this.length >= 26) {
        "$CURRENT_BASE_URL${this.substring(26)}"
    } else {
        "$CURRENT_BASE_URL$this"  // Обрабатываем случаи, когда строка короче 26 символов
    }

}
