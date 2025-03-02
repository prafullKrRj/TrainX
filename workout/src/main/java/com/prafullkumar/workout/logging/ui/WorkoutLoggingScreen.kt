package com.prafullkumar.workout.logging.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutLoggingScreen(
    viewModel: WorkoutLoggingViewModel,
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var caloriesBurned by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${uiState.planName} - ${uiState.splitName}") },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                if (!uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                if (caloriesBurned.trim().toIntOrNull() == null) {
                                    Toast.makeText(
                                        context,
                                        "Please enter valid calories burned",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                } else {
                                    viewModel.finishWorkout()
                                    scope.launch {
                                        viewModel.saveWorkout(caloriesBurned.trim().toInt())
                                        onFinish()
                                    }
                                }
                            },
                            modifier = Modifier.padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Complete Workout")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = caloriesBurned,
                            onValueChange = {
                                caloriesBurned = it
                            },
                            modifier = Modifier.weight(1f),
                            label = { Text("Calories Burned") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
                itemsIndexed(uiState.exercisesToLog) { exerciseIndex, exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        exerciseIndex = exerciseIndex,
                        onSetUpdated = { setIndex, weight, reps, isCompleted ->
                            viewModel.updateSet(
                                exerciseIndex = exerciseIndex,
                                setIndex = setIndex,
                                weight = weight,
                                reps = reps,
                                isCompleted = isCompleted
                            )
                        },
                        onNotesUpdated = { notes ->
                            viewModel.updateExerciseNotes(exerciseIndex, notes)
                        },
                        onSetAdded = {
                            viewModel.addSet(exerciseIndex)
                        }
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Notes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = uiState.notes ?: "",
                                onValueChange = { viewModel.updateWorkoutNotes(it) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Workout notes") },
                                minLines = 3
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: ExerciseUiState,
    exerciseIndex: Int,
    onSetUpdated: (Int, Float?, Int, Boolean) -> Unit,
    onNotesUpdated: (String) -> Unit,
    onSetAdded: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val completedSets = exercise.sets.count { it.isCompleted }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Exercise header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.exerciseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Target: ${exercise.targetSets} sets × ${exercise.targetReps} reps | Rest: ${exercise.restPeriod}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$completedSets/${exercise.sets.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }

            // Exercise sets
            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    exercise.sets.forEachIndexed { setIndex, set ->
                        SetRow(
                            set = set,
                            onSetUpdated = { weight, reps, isCompleted ->
                                onSetUpdated(setIndex, weight, reps, isCompleted)
                            }
                        )
                    }
                    Button(onClick = onSetAdded, Modifier.fillMaxWidth(0.6f)) {
                        Text("Add Set")
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    OutlinedTextField(
                        value = exercise.notes ?: "",
                        onValueChange = onNotesUpdated,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Exercise notes") },
                        singleLine = false,
                        minLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun SetRow(
    set: ExerciseSetUiState,
    onSetUpdated: (Float?, Int, Boolean) -> Unit
) {
    var weight by remember { mutableStateOf(set.weight?.toString() ?: "") }
    var reps by remember { mutableStateOf(set.reps.toString()) }
    var isCompleted by remember { mutableStateOf(set.isCompleted) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set ${set.setNumber}",
            modifier = Modifier.width(48.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
                onSetUpdated(
                    it.toFloatOrNull(),
                    reps.toIntOrNull() ?: set.reps,
                    isCompleted
                )
            },
            modifier = Modifier.weight(1f),
            label = { Text("Weight") },
            singleLine = true
        )

        OutlinedTextField(
            value = reps,
            onValueChange = {
                reps = it
                onSetUpdated(
                    weight.toFloatOrNull(),
                    it.toIntOrNull() ?: set.reps,
                    isCompleted
                )
            },
            modifier = Modifier.weight(1f),
            label = { Text("Reps") },
            singleLine = true
        )

        IconButton(
            onClick = {
                isCompleted = !isCompleted
                onSetUpdated(
                    weight.toFloatOrNull(),
                    reps.toIntOrNull() ?: set.reps,
                    !isCompleted
                )
            },
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (isCompleted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Complete",
                tint = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}