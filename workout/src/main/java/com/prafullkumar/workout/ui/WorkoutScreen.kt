package com.prafullkumar.workout.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.workout.WorkoutRoutes
import com.prafullkumar.workout.ui.customPlans.CustomPlansScreen
import com.prafullkumar.workout.ui.plans.WorkoutPlansScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

enum class WorkoutScreen(
    val value: String
) {
    PLANS("Plans"), CUSTOM_PLANS("Custom Plans"), EXERCISES("Exercises"), EQUIPMENTS("Equipments"), HISTORY(
        "History"
    ),
}

@Composable
fun WorkoutScreen(
    navController: NavController
) {
    val viewModel = getViewModel<WorkoutViewModel>()
    val predefinedPlans by viewModel.preDefinedPlans.collectAsState()
    val pagerState = rememberPagerState {
        5
    }
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize()) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp,
        ) {
            WorkoutScreen.entries.forEachIndexed { index, screen ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = screen.value,
                        modifier = Modifier.padding(14.dp),
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                WorkoutScreen.EXERCISES.ordinal -> ExerciseListScreen(viewModel)
                WorkoutScreen.PLANS.ordinal -> {
                    WorkoutPlansScreen(predefinedPlans, onPlanSelected = {
                        navController.navigate(WorkoutRoutes.WorkoutPlanDetails(it.id))
                    })
                }

                WorkoutScreen.CUSTOM_PLANS.ordinal -> {
                    CustomPlansScreen(viewModel, navController)
                }

                WorkoutScreen.HISTORY.ordinal -> {
                    WorkoutHistoryScreen(viewModel)
                }

                WorkoutScreen.EQUIPMENTS.ordinal -> EquipmentListScreen(viewModel)
            }
        }
    }
}