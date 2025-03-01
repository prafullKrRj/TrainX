package com.prafullkumar.onboarding

import kotlinx.serialization.Serializable

sealed interface OnBoardingRoutes {
    @Serializable
    data object GetStartedScreen : OnBoardingRoutes

    @Serializable
    data object PersonalInfoScreen : OnBoardingRoutes


    @Serializable
    data object ActivityLevelScreen : OnBoardingRoutes


}