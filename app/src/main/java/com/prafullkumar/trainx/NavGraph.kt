package com.prafullkumar.trainx

import kotlinx.serialization.Serializable


sealed interface MainRoutes {

    @Serializable
    data object OnBoarding : MainRoutes

    @Serializable
    data object App : MainRoutes
}

sealed interface AppRoutes {

    @Serializable
    data object Profile : AppRoutes

    @Serializable
    data object Workout : AppRoutes

    @Serializable
    data object Food : AppRoutes

    @Serializable
    data object Home : AppRoutes

    @Serializable
    data object TrainXAi : AppRoutes
}

