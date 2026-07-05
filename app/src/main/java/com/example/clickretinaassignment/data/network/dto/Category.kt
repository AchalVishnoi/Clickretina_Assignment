package com.example.clickretinaassignment.data.network.dto

data class Category(
    val courseCount: Int,
    val courses: List<Course>,
    val description: String,
    val iconColor: String,
    val id: String,
    val name: String
)