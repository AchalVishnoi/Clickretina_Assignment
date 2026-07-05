package com.example.clickretinaassignment.presentation.features.screens

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.clickretinaassignment.data.network.dto.Category
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.presentation.features.Uistate
import com.example.clickretinaassignment.presentation.features.MainViewmodel
import com.example.clickretinaassignment.presentation.features.UiEvent
import com.example.clickretinaassignment.presentation.features.UiIntent
import com.example.clickretinaassignment.utils.toComposeColor
import java.sql.DriverManager

@Composable
fun HomeScreen(
    viewModel: MainViewmodel,
    navigateToCoursePage: () -> Unit,
    navigateToCourseContentPage: () -> Unit
){

    val uistate by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                 UiEvent.NavigateToCoursePage -> navigateToCoursePage()
                 UiEvent.NavigateToCourseContentPage -> navigateToCourseContentPage()
                else -> {}
            }
        }
    }


    if(uistate.isLoading){

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

    }
    else {

        DashboardScreen(
            uistate = uistate,
            searchQuery = uistate.searchQuery,
            onSearchQueryChange = { viewModel.onIntent(UiIntent.SearchQueryChange(it)) },
            onCategoryClick = { viewModel.onIntent(UiIntent.SelectCategory(it)) },
            onCourseClick = { viewModel.onIntent(UiIntent.SelectCourse(it)) },
            onSeeAllCategoriesClick = {},
            onSeeAllCoursesClick = {},
            onNotificationClick = {},
            onProfileClick = {}
        )
    }


}


@Composable
fun DashboardScreen(
    uistate: Uistate,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onCourseClick: (Course) -> Unit,
    onSeeAllCategoriesClick: () -> Unit,
    onSeeAllCoursesClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val allCourses = remember(uistate.categories) {
        uistate.categories.flatMap { it.courses }.distinctBy { it.id }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            DashboardHeader(
                userInitials = "AS",
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )
        }

        item {
            DashboardSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }

        item {
            Column {
                SectionHeader(title = "Categories", onSeeAllClick = onSeeAllCategoriesClick)
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uistate.categories, key = { it.id }) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Popular courses", onSeeAllClick = onSeeAllCoursesClick)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(allCourses, key = { it.id }) { course ->
            CourseCard(
                course = course,
                onClick = {
                    onCourseClick(course)
                    Log.d("Course Selected", "DashboardScreen: ${course.title}")
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DashboardHeader(userInitials: String, onNotificationClick: () -> Unit, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Find your next skill",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInitials,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun DashboardSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search courses, topics...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        ),
        singleLine = true
    )
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "See all",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF00BFA5),
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val composeColor = remember(category.iconColor) { category.iconColor.toComposeColor() }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(composeColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(composeColor, RoundedCornerShape(4.dp))
                )
            }

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.courseCount} courses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    modifier: Modifier = Modifier,
    levelColor: Color = if (course.level.uppercase() == "INTERMEDIATE") Color(0xFFE67E22) else Color(0xFF00BFA5),
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                GlideImage(
                    url = course.thumbnailUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = course.level.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = levelColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = course.instructor.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "★", color = Color(0xFFF39C12), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = course.rating.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🕒", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text(
                            text = "${course.durationHours}h",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlideImage(url: String, modifier: Modifier = Modifier) {
    DriverManager.println(url)
    AndroidView(
        factory = { context ->
            ImageView(context)
        },
        modifier = modifier,
        update = {
            Glide.with(it.context).load(url).into(it)
        }
    )
}