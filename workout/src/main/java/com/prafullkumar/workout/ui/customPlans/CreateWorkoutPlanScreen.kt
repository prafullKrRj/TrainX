package com.prafullkumar.workout.ui.customPlans

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.workout.data.WorkoutSplit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkoutPlanScreen(
    navController: NavController,
    viewModel: com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel
) {
    var showEditNameDialog by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = viewModel.newPlan.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = {
                                showEditNameDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit name"
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                // Plan Details Section
                item {
                    PlanDetailsSection(viewModel)
                }
                itemsIndexed(viewModel.newPlan.splits, key = { index, _ ->
                    index
                }) { index, split ->
                    WorkoutSplitSection(viewModel, index)
                }
                // Add Split Button
                item {
                    Button(
                        onClick = {
                            val splits = viewModel.newPlan.splits.toMutableList()
                            splits.add(
                                WorkoutSplit(
                                    "Day ${splits.size + 1}",
                                    idx = splits.size.toLong()
                                )
                            )
                            viewModel.newPlan = viewModel.newPlan.copy(splits = splits)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE3F2FD),
                            contentColor = Color(0xFF3F51B5)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Split",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Add workout day")
                    }
                }

                // Save Button
                item {
                    Button(
                        onClick = viewModel::savePlan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "SAVE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
    if (showEditNameDialog) {
        EditingDialog(
            currentName = viewModel.newPlan.name,
            onDismiss = { showEditNameDialog = false },
            onNameChange = { newName ->
                viewModel.newPlan = viewModel.newPlan.copy(name = newName)
                showEditNameDialog = false
            }
        )
    }
}

@Composable
fun EditingDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Plan Name") },
        text = {
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Plan Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onNameChange(newName) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}