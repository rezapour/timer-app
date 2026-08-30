package me.rezapour.workout.di

import me.rezapour.workout.presentation.add_workout.viewmodel.AddWorkoutViewModel
import me.rezapour.workout.presentation.my_workouts.mapper.WorkoutItemMapper
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsViewmodel
import me.rezapour.domain.model.Workout
import me.rezapour.ui.mapper.Mapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val addWorkoutModule = module {
    viewModelOf(::AddWorkoutViewModel)
}

private val workoutListModule = module {
    viewModelOf(::MyWorkoutsViewmodel)
    single<Mapper<Workout, WorkoutItem>> { WorkoutItemMapper() }
}

object AddWorkoutModule {
    val modules: List<Module> = listOf(addWorkoutModule, workoutListModule)
}
