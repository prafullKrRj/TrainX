package com.prafullkumar.trainx.home


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prafullkumar.trainx.R
import com.prafullkumar.workout.data.predefinedWorkoutPlans
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HomeTopBar()
        Spacer(modifier = Modifier.height(16.dp))
        CalorieSection(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        WaterProgressSection(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        TodayActivitiesSection(viewModel)
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Today, ${SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun WaterProgressSection(viewModel: HomeViewModel) {
    val waterIntake by viewModel.totalWaterIntake.collectAsState()
    var updateWaterIntakeDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hydration Tracker",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                IconButton(onClick = {
                    updateWaterIntakeDialog = true
                }, modifier = Modifier.size(16.dp)) {
                    Icon(Icons.Default.Create, contentDescription = "Update Water Intake")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            WaterProgress(consumed = waterIntake.toFloat(), target = viewModel.dailyWaterIntake)
            Spacer(modifier = Modifier.height(12.dp))
            WaterControls(viewModel)
        }
    }
    if (updateWaterIntakeDialog) {
        UpdateDailyWaterIntakeDialog(
            currentValue = viewModel.dailyWaterIntake,
            onDismiss = { updateWaterIntakeDialog = false },
            onConfirm = { newValue ->
                viewModel.updateDailyWaterIntake(newValue)
                updateWaterIntakeDialog = false
            }
        )
    }
}

@Composable
private fun UpdateDailyWaterIntakeDialog(
    currentValue: Float,
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var inputValue by remember { mutableStateOf(currentValue.toString()) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Set Daily Water Goal",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                androidx.compose.material3.OutlinedTextField(
                    value = inputValue,
                    onValueChange = {
                        inputValue = it
                        isError = false
                    },
                    label = { Text("Water Goal (L)") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Please enter a valid number between 0.5 and 5") }
                    } else null,
                    trailingIcon = {
                        Text(
                            "L",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = {
                    try {
                        val value = inputValue.toFloat()
                        if (value in 0.5f..5f) {
                            onConfirm(value)
                        } else {
                            isError = true
                        }
                    } catch (e: NumberFormatException) {
                        isError = true
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun WaterProgress(consumed: Float, target: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_water_drop_24),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${consumed}L",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "of ${target}L daily goal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { consumed / target },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun WaterControls(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WaterButton(amount = "150ml", viewModel = viewModel)
        WaterButton(amount = "250ml", viewModel)
        WaterButton(amount = "500ml", viewModel)
    }
}

@Composable
private fun WaterButton(amount: String, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier.width(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        onClick = {
            viewModel.insertHydration(amount.replace("ml", "").toFloat().div(1000f))
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_water_drop_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
private fun TodayActivitiesSection(viewModel: HomeViewModel) {
    val todayFoodLogs by viewModel.todayFoodLogs.collectAsState()
    val todayWorkouts by viewModel.todayWorkouts.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (todayFoodLogs.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Activities",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (todayFoodLogs.isNotEmpty()) {
            Text("Food Logs", style = MaterialTheme.typography.bodyMedium)
            todayFoodLogs.forEach {
                ActivityItem(
                    icon = ImageVector.vectorResource(com.prafullkumar.foodlog.R.drawable.baseline_restaurant_24),
                    title = it.foodName,
                    subtitle = it.mealType,
                    value = "+${it.calories} kcal"
                )
            }
        }
        if (todayWorkouts.isNotEmpty()) {
            Text("Workouts", style = MaterialTheme.typography.bodyMedium)
            todayWorkouts.forEach {
                ActivityItem(
                    icon = ImageVector.vectorResource(R.drawable.baseline_fitness_center_24),
                    title = it.splitName,
                    subtitle = it.planId?.let { id -> predefinedWorkoutPlans[id.toInt()].name }
                        ?: "",
                    value = "-${it.caloriesBurned} kcal"
                )
            }
        }
    }
}

@Composable
private fun ActivityItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (value.startsWith("+")) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CalorieSection(viewModel: HomeViewModel) {
    val todayCalorieEaten by viewModel.todayConsumedCalories.collectAsState()
    val totalCaloriesBurned by viewModel.todayCaloriesBurned.collectAsState()
    val tdee by viewModel.tdeeData.collectAsState()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Daily Calories",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalorieCircle(
                    title = "Consumed",
                    value = todayCalorieEaten.toString(),
                    unit = "kcal",
                    color = MaterialTheme.colorScheme.error
                )
                CalorieCircle(
                    title = "Burned",
                    value = totalCaloriesBurned.toString(),
                    unit = "kcal",
                    color = MaterialTheme.colorScheme.primary
                )
                CalorieCircle(
                    title = "Left",
                    value = (tdee.tdee - todayCalorieEaten + totalCaloriesBurned).toString(),
                    unit = "kcal",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalorieCircle(
                    title = "Carbs",
                    value = tdee.carbs.toString(),
                    unit = "g",
                    color = MaterialTheme.colorScheme.tertiary
                )
                CalorieCircle(
                    title = "Protein",
                    value = tdee.protein.toString(),
                    unit = "g",
                    color = MaterialTheme.colorScheme.primary
                )
                CalorieCircle(
                    title = "Fats",
                    value = tdee.fat.toString(),
                    unit = "g",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CalorieCircle(
    title: String,
    value: String,
    unit: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color.copy(alpha = 0.1f), CircleShape)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = color
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
