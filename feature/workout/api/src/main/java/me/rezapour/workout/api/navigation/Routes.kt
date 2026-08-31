package me.rezapour.workout.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AddEditWorkoutRoute(
    val workoutId: Long? = null
) : NavKey

@Serializable
data object MyWorkoutsRoute : NavKey