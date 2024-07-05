package com.example.socialapp.android.common.util

private const val CURRENT_BASE_URL = "http://192.168.1.138:8080/"


fun String.toCurrentUrl(): String {
    return "$CURRENT_BASE_URL${this.substring(26)}"
}
