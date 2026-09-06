package me.rezapour.workout.di

import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.ui.mapper.Mapper
import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.api.domain.usecase.InsertWorkoutUseCase
import me.rezapour.workout.data.mapper.DataMapper
import me.rezapour.workout.data.mapper.WorkoutDbDataMapper
import me.rezapour.workout.data.repository_impl.WorkoutRepositoryImpl
import me.rezapour.workout.domain.repository.WorkoutRepository
import me.rezapour.workout.domain.usecase.DeleteWorkoutUseCase
import me.rezapour.workout.domain.usecase.GetWorkoutUseCase
import me.rezapour.workout.domain.usecase.GetWorkoutsUseCase
import me.rezapour.workout.domain.usecase.InsertWorkoutUseCaseImpl
import me.rezapour.workout.domain.usecase.UpdateWorkoutUseCase
import me.rezapour.workout.presentation.add_workout.viewmodel.AddEditWorkoutViewModel
import me.rezapour.workout.presentation.my_workouts.mapper.WorkoutItemMapper
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsViewmodel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val presentationModule = module {
    viewModel { params ->
        AddEditWorkoutViewModel(
            formMode = params.get(),
            insertWorkoutUseCase = get(),
            getWorkoutUseCase = get(),
            updateWorkoutUseCase = get(),
            deleteWorkoutUseCase = get()
        )
    }


    viewModelOf(::MyWorkoutsViewmodel)
    single<Mapper<Workout, WorkoutItem>> { WorkoutItemMapper() }
}

val domainModule = module {
    singleOf(::GetWorkoutsUseCase)
    singleOf(::InsertWorkoutUseCaseImpl) bind InsertWorkoutUseCase::class
    singleOf(::DeleteWorkoutUseCase)
    singleOf(::GetWorkoutUseCase)
    singleOf(::UpdateWorkoutUseCase)
}

val dataModule = module {
    singleOf(::WorkoutRepositoryImpl) bind WorkoutRepository::class
    single<DataMapper<WorkoutEntity, Workout>> { WorkoutDbDataMapper() }


}

object AddWorkoutModule {
    val modules: List<Module> =
        listOf(presentationModule, domainModule, dataModule)
}
