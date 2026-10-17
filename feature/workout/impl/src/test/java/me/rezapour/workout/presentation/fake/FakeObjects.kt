package me.rezapour.workout.presentation.fake

import me.rezapour.workout.api.domain.model.Interval
import me.rezapour.workout.api.domain.model.IntervalType
import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem

object WorkoutStubWithTwoItems {
    val workoutListStub = listOf(
        Workout(
            id = 1L,
            name = "Morning Workout",
            rounds = 4,
            intervals = listOf(
                Interval(
                    name = "Workout",
                    durationSeconds = 30L,
                    type = IntervalType.WORK
                ),
                Interval(
                    name = "Rest",
                    durationSeconds = 10L,
                    type = IntervalType.REST
                )
            )
        ),
        Workout(
            id = 2L,
            name = null,
            rounds = 3,
            intervals = listOf(
                Interval(
                    name = "Workout",
                    durationSeconds = 45L,
                    type = IntervalType.WORK
                ),
                Interval(
                    name = "Rest",
                    durationSeconds = 15L,
                    type = IntervalType.REST
                )
            )
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
            rounds = 4,
            intervals = listOf(
                Interval(
                    name = "Workout",
                    durationSeconds = 30L,
                    type = IntervalType.WORK
                ),
                Interval(
                    name = "Rest",
                    durationSeconds = 10L,
                    type = IntervalType.REST
                )
            )
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


