package me.rezapour.workout.presentation.my_workouts.mapper

import me.rezapour.workout.api.domain.model.IntervalType
import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem

class WorkoutItemMapper {
    fun mapDomainToUIModel(domain: Workout): WorkoutItem {
        return WorkoutItem(
            id = domain.id,
            name = domain.name,
            workSeconds = domain.intervals.firstOrNull { it.type == IntervalType.WORK }?.durationSeconds
                ?: 0L,
            restSeconds = domain.intervals.firstOrNull { it.type == IntervalType.REST }?.durationSeconds
                ?: 0L,
            rounds = domain.rounds,
        )
    }

    fun mapDomainToUIModel(domains: List<Workout>): List<WorkoutItem> {
        return domains.map { mapDomainToUIModel(it) }
    }


}
