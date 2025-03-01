package com.prafullkumar.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prafullkumar.onboarding.screens.ActivityLevelScreen
import com.prafullkumar.onboarding.screens.GetStartedScreen
import com.prafullkumar.onboarding.screens.PersonalInfoScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnBoardingNavigation(
    navigateToApp: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel: OnBoardingViewModel = koinViewModel()
    NavHost(navController = navController, startDestination = OnBoardingRoutes.GetStartedScreen) {
        composable<OnBoardingRoutes.GetStartedScreen> {
            GetStartedScreen(viewModel) {
                navController.navigate(OnBoardingRoutes.PersonalInfoScreen)
            }
        }
        composable<OnBoardingRoutes.PersonalInfoScreen> {
            PersonalInfoScreen(viewModel, navController)
        }
        composable<OnBoardingRoutes.ActivityLevelScreen> {
            ActivityLevelScreen(viewModel, navigateToApp)
        }
    }
}