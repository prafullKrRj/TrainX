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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
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
import com.prafullkumar.trainx.ui.theme.TrainXTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainXTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val onBoarded =
        context.getSharedPreferences("trainX", Context.MODE_PRIVATE).getBoolean("onboarded", false)
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
    NavHost(navController = navController, startDestination = AppRoutes.Home) {
        composable<AppRoutes.Home> {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.Home, navController)
            }
        }
        foodLogRoutes(navController)
        composable<AppRoutes.Profile> {
            MainScreen(AppRoutes.Profile, navController)
        }
        composable<AppRoutes.TrainXAi> {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.TrainXAi, navController)
            }
        }
        composable<AppRoutes.Workout> {
            Box(Modifier.fillMaxSize()) {
                MainScreen(AppRoutes.Workout, navController)
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

@Composable
fun MainScreen(destination: Any, navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar(Modifier.fillMaxWidth()) {
            NavigationBarItem(selected = destination == AppRoutes.Home,
                onClick = { navController.navigate(AppRoutes.Home) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home, contentDescription = "Home"
                    )
                },
                label = { Text("Home") })
            NavigationBarItem(selected = destination == AppRoutes.Food,
                onClick = { navController.navigate(AppRoutes.Food) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_food_bank_24),
                        contentDescription = "Food"
                    )
                },
                label = { Text("Food") })
            NavigationBarItem(selected = destination == AppRoutes.Workout,
                onClick = { navController.navigate(AppRoutes.Workout) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.prafullkumar.onboarding.R.drawable.baseline_fitness_center_24), contentDescription = "Workout"
                    )
                },
                label = { Text("Workout") })
            NavigationBarItem(selected = destination == AppRoutes.Profile,
                onClick = { navController.navigate(AppRoutes.Profile) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = "Profile"
                    )
                },
                label = { Text("Profile") })
            NavigationBarItem(selected = destination == AppRoutes.TrainXAi,
                onClick = { navController.navigate(AppRoutes.TrainXAi) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_cloud_queue_24), contentDescription = "TrainXAi"
                    )
                },
                label = { Text("TrainXAi") })
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (destination) {
                AppRoutes.Home -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Home")
                    }
                    // TODO: Add Home screen
                }

                AppRoutes.Food -> {
                    FoodLogMain(navController)
                }

                AppRoutes.Workout -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Workout")
                    }
                    // TODO: Add Workout screen
                }

                AppRoutes.Profile -> {
                    Box(Modifier.fillMaxSize()) {
                        Text("Profile")
                    }
                    // TODO: Add Profile screen
                }

                AppRoutes.TrainXAi -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("TrainXAi")
                    }
                    // TODO: Add TrainXAi screen
                }
            }
        }
    }
}