package com.example.clickretinaassignment.presentation.features.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.clickretinaassignment.data.network.dto.Course
import com.example.clickretinaassignment.data.network.dto.Lesson
import com.example.clickretinaassignment.presentation.features.MainViewmodel
import com.example.clickretinaassignment.presentation.features.Uistate

@Composable
fun CourseContentScreen(
    viewmodel: MainViewmodel,
    onBackClick: () -> Unit
) {
    val uistate by viewmodel.uiState.collectAsState()
    CourseContentScreen(uistate = uistate, onBackClick = onBackClick)
}

@Composable
fun CourseContentScreen(
    uistate: Uistate,
    onBackClick: () -> Unit
) {
    val currentLesson = uistate.selectedCourseContent!!
    val course = uistate.selectedCourse!!
    var currentLessonIndex by remember { mutableStateOf(0) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Lessons", "Notes", "Resources")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        VideoPlayerArea(
            videoUrl = currentLesson.videoUrl, // <-- Successfully handling the actual dynamic videoUrl property
            thumbnailUrl = uistate.selectedCourse?.thumbnailUrl!!,
            currentTimestamp = "02:14",
            totalDuration = String.format("%02d:00", currentLesson.durationMinutes),
            progress = 0.37f,
            onBackClick = onBackClick,
            onFullScreenClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "LESSON ${currentLessonIndex + 1} · ${course.title.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BFA5)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentLesson.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentLesson.content.ifEmpty { "Set up your environment and run your first project file." },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF00BFA5),
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        unselectedContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(course.lessons, key = { _, lesson -> lesson.id }) { index, lesson ->
                            PlaylistLessonCard(
                                lesson = lesson,
                                isPlaying = index == currentLessonIndex,
                                onClick = {
                                    if (lesson.isFree) {
                                        currentLessonIndex = index
                                    }
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
                1 -> PlaceholderTabContent("Your personalized study notes will show up here.")
                2 -> PlaceholderTabContent("Downloadable source files, assets, and documentation codes.")
            }
        }
    }
}

@Composable
fun VideoPlayerArea(
    videoUrl: String, // You can feed this into an ExoPlayer implementation inside an AndroidView factory block later
    thumbnailUrl: String,
    currentTimestamp: String,
    totalDuration: String,
    progress: Float,
    onBackClick: () -> Unit,
    onFullScreenClick: () -> Unit
) {
    // Helpful log statement to track that your dynamic URL endpoints switch on tab switches
    LaunchedEffect(videoUrl) {
        println("VideoPlayer target playing: $videoUrl")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize().alpha(0.4f)) {
            GlideImage(
                url = thumbnailUrl,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Exit Player", tint = Color.White)
            }

            IconButton(
                onClick = onFullScreenClick,
                modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_crop),
                    contentDescription = "Fullscreen toggle",
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play/Pause media video stream",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = currentTimestamp, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color(0xFF00BFA5),
                trackColor = Color.White.copy(alpha = 0.3f),
            )

            Text(text = totalDuration, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
@Composable
fun PlaylistLessonCard(
    lesson: Lesson,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isPlaying) Color(0xFF00BFA5).copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isPlaying) Color(0xFF00BFA5).copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
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
                // Adaptive Action Badge Graphic matching item states inside media list track
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = if (isPlaying) Color(0xFF00BFA5) else if (lesson.isFree) Color(0xFF00BFA5).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPlaying) {
                        // Custom Pause Vector representation drawing when tracks are focused
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_media_pause),
                            contentDescription = "Currently playing",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    } else if (lesson.isFree) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Preview lesson",
                            tint = Color(0xFF00BFA5),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Content details hidden",
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
                        color = if (lesson.isFree || isPlaying) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isPlaying) "Now playing · ${lesson.durationMinutes} min" else "${lesson.durationMinutes} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isPlaying) Color(0xFF00BFA5) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Optional static free text badge tags container structure values
            if (lesson.isFree && !isPlaying) {
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
fun PlaceholderTabContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    }
}