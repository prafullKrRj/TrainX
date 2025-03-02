package com.prafullkumar.workout.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.workout.R
import com.prafullkumar.workout.data.local.ExerciseEntity
import com.prafullkumar.workout.ui.components.ExerciseCard
import com.prafullkumar.workout.ui.components.ExerciseDetailSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    viewModel: WorkoutViewModel
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var currentExercise by remember { mutableStateOf<ExerciseEntity?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedMuscleFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    val exercises by viewModel.exercises.collectAsState()

    // Filter exercises based on search query and selected muscle
    val filteredExercises = exercises.filter { exercise ->
        val matchesSearch = exercise.name.contains(searchQuery, ignoreCase = true)
        val matchesMuscle = selectedMuscleFilter?.let { filter ->
            exercise.muscles.any { it.contains(filter, ignoreCase = true) }
        } ?: true
        matchesSearch && matchesMuscle
    }

    // Get unique muscle groups from all exercises
    val muscleGroups = remember(exercises) {
        exercises.flatMap { it.muscles }.distinct().sorted()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search and Filter Bar
        SearchAndFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedFilter = selectedMuscleFilter,
            onFilterClick = { showFilterDialog = true }
        )

        // Exercise List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                AssistChip(
                    label = {
                        Text(
                            text = "${filteredExercises.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    onClick = {},
                    enabled = false,
                    colors = AssistChipDefaults.assistChipColors(
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_fitness_center_24),
                            contentDescription = "Check",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
            }
            items(filteredExercises, key = {
                it.id
            }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onCardClick = {
                        currentExercise = exercise
                        showBottomSheet = true
                    }
                )
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            muscleGroups = muscleGroups,
            selectedMuscle = selectedMuscleFilter,
            onMuscleSelected = { muscle ->
                selectedMuscleFilter = if (selectedMuscleFilter == muscle) null else muscle
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            ExerciseDetailSheet(
                viewModel = viewModel,
                exercise = currentExercise!!,
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAndFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String?,
    onFilterClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Icon and TextField
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search exercises...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(25),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Transparent,
                    unfocusedContainerColor = Transparent,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                ),
                singleLine = true
            )

            // Filter Button
            IconButton(onClick = onFilterClick) {
                Badge(
                    containerColor = Transparent
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterDialog(
    muscleGroups: List<String>,
    selectedMuscle: String?,
    onMuscleSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter by Muscle Group") },
        text = {
            LazyColumn {
                items(muscleGroups) { muscle ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMuscleSelected(muscle) }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = muscle.lowercase().replace("_", " ")
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (selectedMuscle == muscle) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}