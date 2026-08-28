package me.rezapour.workout.presentation.my_workouts.mapper

import me.rezapour.domain.model.Workout
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem
import me.rezapour.ui.mapper.Mapper

class WorkoutItemMapper : Mapper<Workout, WorkoutItem> {
    override fun mapDomainToUIModel(domain: Workout): WorkoutItem {
        return WorkoutItem(
            id = domain.id,
            name = domain.name,
            workSeconds = domain.workSeconds,
            restSeconds = domain.restSeconds,
            rounds = domain.rounds,
        )
    }

    override fun mapDomainToUIModel(domains: List<Workout>): List<WorkoutItem> {
        return domains.map { mapDomainToUIModel(it) }
    }

    override fun mapUIModelToDomain(item: WorkoutItem): Workout {
        return Workout(
            id = item.id,
            name = item.name,
            workSeconds = item.workSeconds,
            restSeconds = item.restSeconds,
            rounds = item.rounds,
        )
    }

    override fun mapUIModelToDomain(items: List<WorkoutItem>): List<Workout> {
        return items.map { mapUIModelToDomain(it) }
    }
}
