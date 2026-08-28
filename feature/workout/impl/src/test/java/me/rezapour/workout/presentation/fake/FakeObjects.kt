package me.rezapour.workout.presentation.fake

import me.rezapour.domain.model.Workout
import me.rezapour.workout.presentation.workout_list.model.WorkoutItem

object WorkoutStubWithTwoItems {
    val workoutListStub = listOf(
        Workout(
            id = 1L,
            name = "Morning Workout",
            workSeconds = 30L,
            restSeconds = 10L,
            rounds = 4
        ),
        Workout(
            id = 2L,
            name = null,
            workSeconds = 45L,
            restSeconds = 15L,
            rounds = 3
        )
    )

    val workoutItemListExpected = listOf(
        WorkoutItem(
            id = 1L,
            name = "Morning Workout",
            workSeconds = 30L,
            restSeconds = 10L,
            rounds = 4
        ),
        WorkoutItem(
            id = 2L,
            name = null,
            workSeconds = 45L,
            restSeconds = 15L,
            rounds = 3
        )
    )
}

object WorkoutStubWithOneItem {
    val workoutListStub = listOf(
        Workout(
            id = 1L,
            name = "Morning Workout",
            workSeconds = 30L,
            restSeconds = 10L,
            rounds = 4
        )
    )

    val workoutItemListExpected = listOf(
        WorkoutItem(
            id = 1L,
            name = "Morning Workout",
            workSeconds = 30L,
            restSeconds = 10L,
            rounds = 4
        )
    )
}


