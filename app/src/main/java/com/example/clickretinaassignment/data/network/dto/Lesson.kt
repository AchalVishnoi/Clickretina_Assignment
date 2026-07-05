package com.example.clickretinaassignment.data.network.dto

data class Lesson(
    val content: String,
    val durationMinutes: Int,
    val id: String,
    val isFree: Boolean,
    val title: String,
    val videoUrl: String
)