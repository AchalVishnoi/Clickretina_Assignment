package com.example.clickretinaassignment.presentation.features

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickretinaassignment.data.network.dto.Category
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Lesson
import com.example.clickretinaassignment.data.repository.RepositoryImpl
import com.example.clickretinaassignment.domain.repository.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewmodel() : ViewModel() {

    private val _uiState = MutableStateFlow(Uistate())
    val uiState: StateFlow<Uistate> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent: SharedFlow<UiEvent?> = _uiEvent.asSharedFlow()

    init {
        loadData()
    }

    fun onIntent(intent: UiIntent){
        when(intent){
            is UiIntent.LoadData -> loadData()
            is UiIntent.SearchCourses -> searchCourses(intent.query)
            is UiIntent.SearchQueryChange -> searchQueryChange(intent.query)
            is UiIntent.SelectCategory -> selectCategory(intent.category)
            is UiIntent.SelectCourse -> selectCourse(intent.course)
            is UiIntent.SelectCourseContent -> selectCourseContent(intent.content)

        }

    }

    private fun loadData(){
        val repository = RepositoryImpl()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            val result = repository.getAllDetails()
            result.fold(
                onSuccess = {
                    Log.d( "Api calling", "loadData: ${it.toString()}")
                    _uiState.value = _uiState.value.copy(
                        categories = it.categories,
                    )
                },
                onFailure = {
                    Log.e( "Api calling", "loadData: ${it.toString()}")
                    _uiState.value = _uiState.value.copy(
                        categories = emptyList()
                    )
                    _uiEvent.emit(UiEvent.ShowToast("Failed to load data"))
                }
            )
            _uiState.value = _uiState.value.copy(
                isLoading = false
            )

        }
    }
    private fun searchCourses(query: String){}
    private fun searchQueryChange(query: String){
        _uiState.value = _uiState.value.copy(
            searchQuery = query
        )
    }
    private fun selectCategory(category: Category){
        _uiState.value = _uiState.value.copy(
            slectedCategory = category
        )

    }
    private fun selectCourse(course: Course){
        viewModelScope.launch {
            Log.d("Course selected", "selectCourse: $course")
            _uiState.value = _uiState.value.copy(
                selectedCourse = course
            )
            _uiEvent.emit(UiEvent.NavigateToCoursePage)
        }

    }
    private fun selectCourseContent(content: Lesson){
        viewModelScope.launch {
            Log.d("Course content selected", "selectCourseContent: $content")
            _uiState.value = _uiState.value.copy(
                selectedCourseContent = content
            )
            _uiEvent.emit(UiEvent.NavigateToCourseContentPage)
        }
    }


}