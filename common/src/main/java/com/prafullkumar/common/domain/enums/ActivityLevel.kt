package com.prafullkumar.common.domain.enums

enum class ActivityLevel(val value: String, val description: String, val emoji: String) {
    SEDENTARY(
        "Sedentary",
        "Little to no exercise, mostly sitting throughout the day (e.g., desk job, minimal movement).",
        "🛋️"
    ),
    LIGHTLY_ACTIVE(
        "Lightly Active",
        "Light daily activity or low-intensity exercise 1-3 times a week (e.g., walking, household chores).",
        "🚶"
    ),
    MODERATELY_ACTIVE(
        "Moderately Active",
        "Moderate exercise or sports 3-5 days a week (e.g., jogging, cycling, gym workouts).",
        "🏃"
    ),
    VERY_ACTIVE(
        "Very Active",
        "Hard exercise or sports 6-7 days a week, physically demanding job (e.g., construction worker, athlete).",
        "🏋️"
    ),
    SUPER_ACTIVE(
        "Super Active",
        "Very intense exercise daily or a highly active job (e.g., professional athlete, military training).",
        "🔥"
    )
}