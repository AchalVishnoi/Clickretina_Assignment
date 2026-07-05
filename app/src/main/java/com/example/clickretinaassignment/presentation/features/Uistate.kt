package com.example.clickretinaassignment.presentation.features

import com.example.clickretinaassignment.data.network.dto.Category
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Lesson

data class Uistate(
    val categories: List<Category> = emptyList(),
    val popularCourses: List<Course> = emptyList(),
    val searchQuery: String="",
    val slectedCategory: Category? = null,
    val selectedCourse: Course? = null,
    val selectedCourseContent: Lesson?=null,
    val isLoading: Boolean = false
)