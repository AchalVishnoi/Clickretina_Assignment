package com.example.clickretinaassignment.utils

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(): Color {
    return try {
        val cleanedHex = this.replace("#", "")
        if (cleanedHex.length == 6) {
            Color(android.graphics.Color.parseColor("#$cleanedHex"))
        } else {
            Color(android.graphics.Color.parseColor("#$cleanedHex"))
        }
    } catch (e: Exception) {
        Color.Gray
    }
}