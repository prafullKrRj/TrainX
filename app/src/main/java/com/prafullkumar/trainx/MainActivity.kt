package com.prafullkumar.trainx

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prafullkumar.foodlog.FoodRoutes
import com.prafullkumar.foodlog.ui.AddFoodScreen
import com.prafullkumar.foodlog.ui.FoodLogMain
import com.prafullkumar.foodlog.ui.foodHistory.FoodHistoryScreen
import com.prafullkumar.onboarding.OnBoardingNavigation
import com.prafullkumar.profile.ProfileScreen
import com.prafullkumar.trainx.home.HomeScreen
import com.prafullkumar.trainx.ui.theme.TrainXTheme
import com.prafullkumar.trainxai.AIScreen
import com.prafullkumar.trainxai.AIViewModel
import com.prafullkumar.workout.WorkoutRoutes
import com.prafullkumar.workout.logging.ui.WorkoutLoggingScreen
import com.prafullkumar.workout.ui.WorkoutScreen
import com.prafullkumar.workout.ui.customPlans.CreateWorkoutPlanScreen
import com.prafullkumar.workout.ui.planDetailScreen.WorkoutPlanScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainXTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val onBoarded = context.getSharedPreferences("trainX", Context.MODE_PRIVATE)
        .getBoolean("onboarded", false)
    NavHost(
        startDestination = if (onBoarded) MainRoutes.App else MainRoutes.OnBoarding,
        navController = navController
    ) {
        composable<MainRoutes.OnBoarding> {
            OnBoardingNavigation {
                navController.navigate(MainRoutes.App) {
                    popUpTo(MainRoutes.OnBoarding) {
                        inclusive = true
                    }
                }
            }
        }
        composable<MainRoutes.App> {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModels = rememberSaveable {
        mutableMapOf<Any, ViewModel>()
    }
    NavHost(navController = navController, startDestination = AppRoutes.Home) {
        workoutRoutes(navController)
        composable<AppRoutes.Home> {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.Home, navController, viewModels)
            }
        }
        foodLogRoutes(navController)
        composable<AppRoutes.Profile> {
            MainScreen(AppRoutes.Profile, navController, viewModels)
        }
        composable<AppRoutes.TrainXAi> {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.TrainXAi, navController, viewModels)
            }
        }
    }
}

fun NavGraphBuilder.foodLogRoutes(navController: NavController) {
    navigation<AppRoutes.Food>(startDestination = FoodRoutes.Main) {
        composable<FoodRoutes.Main> {
            MainScreen(AppRoutes.Food, navController)
        }
        composable<FoodRoutes.AddFood> {
            val mealType = it.toRoute<FoodRoutes.AddFood>().mealType
            AddFoodScreen(koinViewModel { parametersOf(mealType) }, navController)
        }
        composable<FoodRoutes.FoodHistory> {
            FoodHistoryScreen(koinViewModel(), navController)
        }
        composable<FoodRoutes.FoodDetails> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Food Details")
            }
        }
    }
}

fun NavGraphBuilder.workoutRoutes(navController: NavController) {
    navigation<AppRoutes.Workout>(startDestination = WorkoutRoutes.Main) {
        composable<WorkoutRoutes.Main> {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.Workout, navController)
            }
        }
        composable<WorkoutRoutes.CreateWorkoutPlan> {
            CreateWorkoutPlanScreen(navController, koinViewModel())
        }
        composable<WorkoutRoutes.WorkoutPlanDetails> {
            WorkoutPlanScreen(koinViewModel(), navController)
        }
        composable<WorkoutRoutes.LogWorkout> {
            WorkoutLoggingScreen(koinViewModel()) {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun MainScreen(
    destination: Any,
    navController: NavController,
    viewModels: MutableMap<Any, ViewModel> = mutableMapOf()
) {
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar(Modifier.fillMaxWidth()) {
            NavigationBarItem(
                selected = destination == AppRoutes.Home,
                onClick = { navController.navigate(AppRoutes.Home) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home, contentDescription = "Home"
                    )
                },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = destination == AppRoutes.Food,
                onClick = { navController.navigate(AppRoutes.Food) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_food_bank_24),
                        contentDescription = "Food"
                    )
                },
                label = { Text("Food") }
            )
            NavigationBarItem(
                selected = destination == AppRoutes.Workout,
                onClick = { navController.navigate(AppRoutes.Workout) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_fitness_center_24),
                        contentDescription = "Workout"
                    )
                },
                label = { Text("Workout") }
            )
            NavigationBarItem(
                selected = destination == AppRoutes.Profile,
                onClick = { navController.navigate(AppRoutes.Profile) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = "Profile"
                    )
                },
                label = { Text("Profile") }
            )
            NavigationBarItem(
                selected = destination == AppRoutes.TrainXAi,
                onClick = { navController.navigate(AppRoutes.TrainXAi) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_cloud_queue_24),
                        contentDescription = "TrainXAi"
                    )
                },
                label = { Text("TrainXAi") }
            )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (destination) {
                AppRoutes.Home -> {
                    HomeScreen(koinViewModel())
                }

                AppRoutes.Food -> {
                    FoodLogMain(navController)
                }

                AppRoutes.Workout -> {
                    WorkoutScreen(navController)
                }

                AppRoutes.Profile -> {
                    ProfileScreen(navController)
                }

                AppRoutes.TrainXAi -> {
                    viewModels.putIfAbsent(AppRoutes.TrainXAi, koinViewModel<AIViewModel>())
                    AIScreen(viewModels[AppRoutes.TrainXAi] as AIViewModel)
                }
            }
        }
    }
}