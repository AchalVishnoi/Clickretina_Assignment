package com.example.clickretinaassignment

import com.example.clickretinaassignment.data.network.dto.Category
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Instructor
import com.example.clickretinaassignment.presentation.features.MainViewmodel
import com.example.clickretinaassignment.presentation.features.UiEvent
import com.example.clickretinaassignment.presentation.features.UiIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewmodel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewmodel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun typingSearchQuery_intentFired_updatesSearchQueryInUiState() = runTest {

        val typedText = "Jetpack Compose"

        viewModel.onIntent(UiIntent.SearchQueryChange(typedText))
        advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertEquals(typedText, currentState.searchQuery)
    }

    @Test
    fun selectingCourse_intentFired_updatesStateAndEmitsNavigationEvent() = runTest {

        val mockCourse = Course(
            id = "course_abc",
            title = "Kotlin for Beginners",
            subtitle = "Learn modern Android syntax",
            description = "Complete masterclass tutorial",
            level = "Beginner",
            rating = 4.8,
            durationHours = 12.5,
            studentsEnrolled = 1500,
            language = "English",
            lastUpdated = "2026",
            thumbnailUrl = "https://example.com/image.png",
            instructor = Instructor("avatar", "Bio text", "ins_1", "Jane Doe", "Lead Dev"),
            lessons = emptyList(),
            tags = emptyList()
        )

        val receivedEvents = mutableListOf<UiEvent?>()
        val job = launch(testDispatcher) {
            viewModel.uiEvent.collect { receivedEvents.add(it) }
        }

        viewModel.onIntent(UiIntent.SelectCourse(mockCourse))
        advanceUntilIdle()

        assertEquals(mockCourse, viewModel.uiState.value.selectedCourse)

        assertEquals(1, receivedEvents.size)
        assertEquals(UiEvent.NavigateToCoursePage, receivedEvents.first())

        job.cancel()
    }
}