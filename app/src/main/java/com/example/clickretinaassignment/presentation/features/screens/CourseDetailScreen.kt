package com.example.clickretinaassignment.presentation.features.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Lesson
import com.example.clickretinaassignment.presentation.features.MainViewmodel
import com.example.clickretinaassignment.presentation.features.UiEvent
import com.example.clickretinaassignment.presentation.features.UiIntent


@Composable
fun CourseDetailScreen(
    viewModel: MainViewmodel,
    onBackClick: () -> Unit,
    navigateToCourseContentPage: () -> Unit
){

    val uistate by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateToCourseContentPage -> navigateToCourseContentPage()
                else -> {}
            }
        }
    }
    CourseDetailScreen(
        course = uistate.selectedCourse!!,
        onBackClick = onBackClick,
        onBookmarkClick = {},
        onFollowInstructorClick = {},
        onLessonClick = { lesson ->
            viewModel.onIntent(UiIntent.SelectCourseContent(lesson))
        },
        onEnrollClick = {},
    )


}



@Composable
fun CourseDetailScreen(
    course: Course,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onFollowInstructorClick: () -> Unit,
    onLessonClick: (Lesson) -> Unit,
    onEnrollClick: () -> Unit
) {
    val totalLessons = course.lessons.size
    val totalDurationMinutes = course.lessons.sumOf { it.durationMinutes }

    Scaffold(
        bottomBar = {
            EnrollBottomBar(
                isFree = course.lessons.all { it.isFree },
                onEnrollClick = onEnrollClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            item {
                CourseHeaderBanner(
                    thumbnailUrl = course.thumbnailUrl,
                    onBackClick = onBackClick,
                    onBookmarkClick = onBookmarkClick
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = course.subtitle.ifEmpty { course.description },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    CourseMetricsRow(course = course)

                    Spacer(modifier = Modifier.height(8.dp))

                    InstructorProfileCard(
                        instructorName = course.instructor.name,
                        instructorTitle = course.instructor.title,
                        avatarUrl = course.instructor.avatarUrl,
                        onFollowClick = onFollowInstructorClick
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CourseContentSectionHeader(
                        lessonCount = totalLessons,
                        durationMinutes = totalDurationMinutes
                    )
                }
            }

            items(course.lessons, key = { it.id }) { lesson ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)) {
                    LessonItemCard(
                        lesson = lesson,
                        onClick = { if (lesson.isFree) onLessonClick(lesson) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
@Composable
fun CourseHeaderBanner(
    thumbnailUrl: String,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        GlideImage(
            url = thumbnailUrl,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back",
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Save course",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun CourseMetricsRow(course: Course) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "★", color = Color(0xFFF39C12), style = MaterialTheme.typography.bodyMedium)
            Text(text = course.rating.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "${String.format("%,d", course.studentsEnrolled)} Enrolled",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "🕒", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            Text(text = "${course.durationHours}h", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }

        Text(
            text = course.level.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (course.level.uppercase() == "INTERMEDIATE") Color(0xFFE67E22) else Color(0xFF00BFA5)
        )
    }
}

@Composable
fun InstructorProfileCard(
    instructorName: String,
    instructorTitle: String,
    avatarUrl: String,
    onFollowClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    GlideImage(url = avatarUrl, modifier = Modifier.fillMaxSize())
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = instructorName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = instructorTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = "Follow",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BFA5),
                modifier = Modifier
                    .clickable { onFollowClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun CourseContentSectionHeader(lessonCount: Int, durationMinutes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Course content",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "$lessonCount lessons • ${durationMinutes} min",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
@Composable
fun LessonItemCard(
    lesson: Lesson,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Interactive dynamic icon base logic shape
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = if (lesson.isFree) Color(0xFF00BFA5).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (lesson.isFree) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play lesson preview",
                            tint = Color(0xFF00BFA5),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Lesson Locked",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (lesson.isFree) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${lesson.durationMinutes} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Render absolute optional FREE tag pills directly
            if (lesson.isFree) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF00BFA5).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00BFA5),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun EnrollBottomBar(
    isFree: Boolean,
    onEnrollClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PRICE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Text(
                    text = if (isFree) "Free" else "Premium",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BFA5)
                )
            }

            Button(
                onClick = onEnrollClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(54.dp)
                    .weight(1f)
                    .padding( some = 24.dp, start = 32.dp ) // Allocating clean separation logic margins
            ) {
                Text(
                    text = "Enroll now",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
private fun Modifier.padding(some: Dp, start: Dp): Modifier {
    return this.padding(start = start)
}