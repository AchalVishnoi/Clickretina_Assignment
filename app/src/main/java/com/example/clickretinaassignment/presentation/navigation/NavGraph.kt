package com.example.clickretinaassignment.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clickretinaassignment.presentation.features.MainViewmodel
import com.example.clickretinaassignment.presentation.features.screens.CourseContentScreen
import com.example.clickretinaassignment.presentation.features.screens.CourseDetailScreen
import com.example.clickretinaassignment.presentation.features.screens.HomeScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewmodel>()

    NavHost(
        navController = navController,
        startDestination = "HomeScreen"
    ){

        composable("HomeScreen"){
            HomeScreen(
                viewModel = viewModel,
                navigateToCoursePage = {
                    Log.d("NavGraph", "NavGraph: Navigate to course page called")
                    navController.navigate("CourseScreen")
                                       },
                navigateToCourseContentPage = { navController.navigate("CourseContentScreen") }
            )
        }
        composable("CourseScreen"){
            CourseDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                navigateToCourseContentPage = { navController.navigate("CourseContentScreen") }
            )

        }
        composable("CourseContentScreen"){
            CourseContentScreen(
                viewmodel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}