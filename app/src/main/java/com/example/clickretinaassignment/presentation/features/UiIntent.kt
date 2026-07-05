package com.example.clickretinaassignment.presentation.features

import com.example.clickretinaassignment.data.network.dto.Category
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Lesson

sealed class UiIntent {
    object LoadData : UiIntent()
    data class SearchCourses(val query: String) : UiIntent()
    data class SearchQueryChange(val query: String) : UiIntent()
    data class SelectCategory(val category: Category) : UiIntent()
    data class SelectCourse(val course: Course) : UiIntent()
    data class SelectCourseContent(val content: Lesson) : UiIntent()
}