package com.example.clickretinaassignment.data.network.dto

data class Course(
    val description: String,
    val durationHours: Double,
    val id: String,
    val instructor: Instructor,
    val language: String,
    val lastUpdated: String,
    val lessons: List<Lesson>,
    val level: String,
    val rating: Double,
    val studentsEnrolled: Int,
    val subtitle: String,
    val tags: List<String>,
    val thumbnailUrl: String,
    val title: String
)