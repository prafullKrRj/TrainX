package com.prafullkumar.workout.ui.customPlans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prafullkumar.workout.data.WeightReps
import com.prafullkumar.workout.data.WorkoutExercise
import com.prafullkumar.workout.data.exercise.exercises


@Composable
fun WorkoutSplitSection(
    viewModel: CreatePlanViewModel,
    index: Int
) {
    var showEditSplitNameDialog by remember { mutableStateOf(false) }
    var showEditSplitDescriptionDialog by remember { mutableStateOf(false) }
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SplitHeading(viewModel, index) {
                showEditSplitNameDialog = it
            }
            Row(Modifier.fillMaxWidth()) {
                Text("Description: ${viewModel.newPlan.splits[index].description}")
                IconButton(
                    onClick = {
                        showEditSplitDescriptionDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit description"
                    )
                }
            }
            viewModel.newPlan.splits[index].exercises.forEachIndexed { exerciseIndex, exercise ->
                ExerciseRow(exercise, index, exerciseIndex, viewModel)
            }
            AddExerciseButton(viewModel, index)
        }
    }
    if (showEditSplitNameDialog) {
        EditingDialog(
            currentName = viewModel.newPlan.splits[index].name,
            onDismiss = { showEditSplitNameDialog = false },
            onNameChange = { newName ->
                viewModel.newPlan.splits[index] =
                    viewModel.newPlan.splits[index].copy(name = newName)
                showEditSplitNameDialog = false
            }
        )
    }
    if (showEditSplitDescriptionDialog) {
        EditingDialog(
            currentName = viewModel.newPlan.splits[index].description,
            onDismiss = { showEditSplitDescriptionDialog = false },
            onNameChange = { newDescription ->
                viewModel.newPlan.splits[index] =
                    viewModel.newPlan.splits[index].copy(description = newDescription)
                showEditSplitDescriptionDialog = false
            }
        )
    }
}

@Composable
fun SplitDescription(description: String) {

}

@Composable
private fun ExerciseRow(
    exercise: WorkoutExercise,
    splitIndex: Int,
    exerciseIndex: Int,
    viewModel: com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel
) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    exercises[exercise.exercise].name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        val updatedSplits = viewModel.newPlan.splits.toMutableList()
                        val updatedExercises = updatedSplits[splitIndex].exercises.toMutableList()
                        updatedExercises.removeAt(exerciseIndex)
                        updatedSplits[splitIndex] =
                            updatedSplits[splitIndex].copy(exercises = updatedExercises)
                        viewModel.newPlan = viewModel.newPlan.copy(splits = updatedSplits)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete exercise",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Divider(
                Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            exercise.weightReps.forEachIndexed { setIndex, weightRep ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Set ${setIndex + 1}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = weightRep.weight.toString(),
                            onValueChange = { newValue ->
                                val newWeight = newValue.toFloatOrNull() ?: weightRep.weight
                                viewModel.newPlan = viewModel.newPlan.copy(
                                    splits = viewModel.newPlan.splits.toMutableList().apply {
                                        this[splitIndex] = this[splitIndex].copy(
                                            exercises = this[splitIndex].exercises.toMutableList()
                                                .apply {
                                                    this[exerciseIndex] = this[exerciseIndex].copy(
                                                        weightReps = this[exerciseIndex].weightReps.toMutableList()
                                                            .apply {
                                                                this[setIndex] =
                                                                    weightRep.copy(weight = newWeight)
                                                            }
                                                    )
                                                }
                                        )
                                    }
                                )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = weightRep.reps.toString(),
                            onValueChange = { newValue ->
                                val newReps = newValue.toIntOrNull() ?: weightRep.reps
                                val updatedSplits = viewModel.newPlan.splits.toMutableList()
                                val updatedExercises =
                                    updatedSplits[splitIndex].exercises.toMutableList()
                                val updatedWeightReps =
                                    updatedExercises[exerciseIndex].weightReps.toMutableList()
                                updatedWeightReps[setIndex] = weightRep.copy(reps = newReps)
                                updatedExercises[exerciseIndex] =
                                    updatedExercises[exerciseIndex].copy(weightReps = updatedWeightReps)
                                updatedSplits[splitIndex] =
                                    updatedSplits[splitIndex].copy(exercises = updatedExercises)
                                viewModel.newPlan = viewModel.newPlan.copy(splits = updatedSplits)
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                // Only allow deletion if there's more than one set
                                if (exercise.weightReps.size > 1) {
                                    val updatedSplits = viewModel.newPlan.splits.toMutableList()
                                    val updatedExercises =
                                        updatedSplits[splitIndex].exercises.toMutableList()
                                    val updatedWeightReps =
                                        updatedExercises[exerciseIndex].weightReps.toMutableList()

                                    updatedWeightReps.removeAt(setIndex)
                                    updatedExercises[exerciseIndex] =
                                        updatedExercises[exerciseIndex].copy(weightReps = updatedWeightReps)
                                    updatedSplits[splitIndex] =
                                        updatedSplits[splitIndex].copy(exercises = updatedExercises)
                                    viewModel.newPlan =
                                        viewModel.newPlan.copy(splits = updatedSplits)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete set",
                                tint = if (exercise.weightReps.size > 1)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.3f
                                    )
                            )

                        }
                    }
                }
            }

            FilledTonalButton(
                onClick = {
                    val updatedSplits = viewModel.newPlan.splits.toMutableList()
                    val updatedExercises = updatedSplits[splitIndex].exercises.toMutableList()
                    val updatedWeightReps =
                        updatedExercises[exerciseIndex].weightReps.toMutableList()

                    updatedWeightReps.add(WeightReps(weight = 5.0f, reps = 10))
                    updatedExercises[exerciseIndex] =
                        updatedExercises[exerciseIndex].copy(weightReps = updatedWeightReps)
                    updatedSplits[splitIndex] =
                        updatedSplits[splitIndex].copy(exercises = updatedExercises)
                    viewModel.newPlan = viewModel.newPlan.copy(splits = updatedSplits)
                },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add set",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Add set")
            }
        }

    }
}

@Composable
fun AddExerciseButton(
    viewModel: com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel,
    index: Int
) {
    var showExerciseSelector by remember { mutableStateOf(false) }
    Button(onClick = {
        showExerciseSelector = true
    }) {
        Icon(Icons.Default.Add, contentDescription = "Add exercise")
        Text("Add exercise")
    }
    if (showExerciseSelector) {
        ExerciseSelectorDialog(
            onDismiss = { showExerciseSelector = false },
            onExerciseSelected = { exercise ->
                // Create completely new lists to ensure state changes are detected
                val updatedSplits = viewModel.newPlan.splits.toMutableList()
                val updatedExercises = updatedSplits[index].exercises.toMutableList()

                // Add the new exercise
                updatedExercises.add(
                    WorkoutExercise(
                        exercise = exercise.id,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 5.0f, reps = 10)
                        ),
                        restPeriod = 60
                    )
                )

                // Update the split with new exercises list
                updatedSplits[index] = updatedSplits[index].copy(exercises = updatedExercises)

                // Update the plan with new splits list
                viewModel.newPlan = viewModel.newPlan.copy(splits = updatedSplits)
                showExerciseSelector = false
            }
        )
    }
}

@Composable
private fun SplitHeading(
    viewModel: com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel,
    index: Int,
    showEditSplitNameDialog: (Boolean) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(viewModel.newPlan.splits[index].emoji)
        Spacer(modifier = Modifier.width(8.dp))
        Text(viewModel.newPlan.splits[index].name)
        IconButton(
            onClick = {
                showEditSplitNameDialog(true)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit name"
            )
        }
    }
}