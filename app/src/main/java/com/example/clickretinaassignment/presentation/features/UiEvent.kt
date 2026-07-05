package com.example.clickretinaassignment.presentation.features

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data object NavigateToCoursePage : UiEvent()
    data object NavigateToCourseContentPage : UiEvent()
}