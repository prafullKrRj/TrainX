package com.prafullkumar.workout.ui.customPlans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.workout.WorkoutRoutes
import com.prafullkumar.workout.ui.WorkoutViewModel
import com.prafullkumar.workout.ui.plans.WorkoutPlanCard

@Composable
fun CustomPlansScreen(
    viewModel: WorkoutViewModel,
    navController: NavController
) {
    val customPlans by viewModel.customPlans.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(WorkoutRoutes.CreateWorkoutPlan)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Workout Plan")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(customPlans, key = { idx, _ ->
                idx
            }) { _, plan ->
                WorkoutPlanCard(plan = plan, onPlanSelected = {
                    navController.navigate(WorkoutRoutes.WorkoutPlanDetails(plan.id))
                })
            }
        }
    }
}