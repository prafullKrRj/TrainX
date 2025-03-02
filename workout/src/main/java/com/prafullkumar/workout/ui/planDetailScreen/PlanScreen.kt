package com.prafullkumar.workout.ui.planDetailScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.prafullkumar.workout.R
import com.prafullkumar.workout.WorkoutRoutes
import com.prafullkumar.workout.data.WorkoutExercise
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutSplit
import com.prafullkumar.workout.data.exercise.exercises

@Composable
fun WorkoutPlanScreen(
    viewModel: com.prafullkumar.workout.ui.planDetailScreen.PlanDetailViewModel,
    navController: NavController
) {
    val workoutPlan by viewModel.state.collectAsState()
    if (workoutPlan != null) {
        WorkoutPlanDetailScreen(
            workoutPlan = workoutPlan!!,
            onBackClick = navController::popBackStack,
            navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanDetailScreen(
    workoutPlan: WorkoutPlan,
    onBackClick: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val planColor = workoutPlan.color
    val surfaceColor = MaterialTheme.colorScheme.surface

    // Create gradient colors based on the plan's color
    val gradientColors = listOf(
        Color(planColor.toArgb()).copy(alpha = 0.9f),
        Color(planColor.toArgb()).copy(alpha = 0.5f),
        surfaceColor
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Background image with blur and overlay
        workoutPlan.imageUrl?.let { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(8.dp),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay on top of background image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = gradientColors,
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = 1000f
                    )
                )
        )

        // Main content
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Plan header
                item {
                    PlanHeader(workoutPlan)
                }

                // Plan metadata cards
                item {
                    PlanMetadataSection(workoutPlan)
                }

                // Plan description
                item {
                    if (workoutPlan.description.isNotEmpty()) {
                        PlanDescriptionCard(workoutPlan.description)
                    }
                }

                // Splits section title
                item {
                    Text(
                        text = "Workout Splits",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Workout splits
                items(workoutPlan.splits) { split ->
                    WorkoutSplitCard(split, workoutPlan.id, navController)
                }
            }
        }
    }
}

@Composable
fun PlanHeader(workoutPlan: WorkoutPlan) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Tag showing plan category
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = workoutPlan.category.name,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // Plan name
        Text(
            text = workoutPlan.name,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Plan type with emoji
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = workoutPlan.type.getIcon(),
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = workoutPlan.type.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun PlanMetadataSection(workoutPlan: WorkoutPlan) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Frequency card
        MetadataCard(
            title = "Frequency",
            value = "${workoutPlan.frequency}x per week",
            modifier = Modifier.weight(1f)
        )

        // Level card
        val levelColor = workoutPlan.level.getColor()
        MetadataCard(
            title = "Level",
            value = workoutPlan.level.name,
            valueColor = Color(levelColor.toArgb()),
            modifier = Modifier.weight(1f)
        )

        // Splits count card
        MetadataCard(
            title = "Splits",
            value = "${workoutPlan.splits.size}",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MetadataCard(
    title: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor.takeIf { it != Color.Unspecified }
                        ?: MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun PlanDescriptionCard(description: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Plan Description",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) ImageVector.vectorResource(R.drawable.baseline_expand_less_24) else ImageVector.vectorResource(
                            R.drawable.baseline_expand_more_24
                        ),
                        contentDescription = if (expanded) "Show less" else "Show more"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WorkoutSplitCard(split: WorkoutSplit, planId: Long, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier

                    .animateContentSize()
            ) {
                // Split header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = split.emoji,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = split.name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${split.exercises.size} exercises",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = if (expanded) ImageVector.vectorResource(R.drawable.baseline_expand_less_24) else ImageVector.vectorResource(
                            R.drawable.baseline_expand_more_24
                        ),
                        contentDescription = if (expanded) "Show less" else "Show more"
                    )
                }

                // Exercises list (visible when expanded)
                if (expanded) {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )

                    if (split.description.isNotEmpty()) {
                        Text(
                            text = split.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )

                        Divider(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }

                    split.exercises.forEachIndexed { index, exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            index = index + 1,
                            isLast = index == split.exercises.size - 1
                        )
                    }
                }
            }
        }
        FilledTonalIconButton(onClick = {
            navController.navigate(WorkoutRoutes.LogWorkout(planId, split.idx))
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "To Log")
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: WorkoutExercise,
    index: Int,
    isLast: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = exercises[exercise.exercise].name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.weight(1f)
            )

            // Rest period
            val restMinutes = exercise.restPeriod / 60
            val restSeconds = exercise.restPeriod % 60
            val restText = if (restMinutes > 0) {
                "$restMinutes:${restSeconds.toString().padStart(2, '0')} min"
            } else {
                "$restSeconds sec"
            }

            Text(
                text = "Rest: $restText",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (expanded) ImageVector.vectorResource(R.drawable.baseline_expand_less_24) else ImageVector.vectorResource(
                    R.drawable.baseline_expand_more_24
                ),
                contentDescription = if (expanded) "Show less" else "Show more",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }

        // Sets and reps details
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                exercise.weightReps.forEachIndexed { setIndex, set ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Set ${setIndex + 1}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.width(50.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "${set.weight} kg",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${set.reps} reps",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Divider between exercises
        if (!isLast) {
            Divider(
                modifier = Modifier.padding(start = 60.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                thickness = 1.dp
            )
        }
    }
}
