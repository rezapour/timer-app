package me.rezapour.workout.presentation.workout_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.rezapour.workout.presentation.workout_list.model.WorkoutItem
import me.rezapour.domain.model.Workout
import me.rezapour.domain.usecase.DeleteWorkoutUseCase
import me.rezapour.domain.usecase.GetWorkoutsUseCase
import me.rezapour.ui.mapper.Mapper

class WorkoutListViewModel(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val mapper: Mapper<Workout, WorkoutItem>
) : ViewModel() {

    private val _uiEffect: MutableSharedFlow<WorkoutListUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<WorkoutListUiEffect> = _uiEffect.asSharedFlow()

    val uiState: StateFlow<WorkoutListUiState> = getWorkoutsUseCase()
        .map { workouts ->
            WorkoutListUiState(
                workouts = mapper.mapDomainToUIModel(workouts)
            )
        }
        .onStart { emit(WorkoutListUiState(isLoading = true)) }
        .catch { error ->
            emit(WorkoutListUiState(isLoading = false))
            emitShowSankBarEffect(error.message.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WorkoutListUiState()
        )


    fun onAction(action: WorkoutListAction) {
        when (action) {
            WorkoutListAction.BackPress -> emitBackNavigation()
            is WorkoutListAction.DeletePress -> deleteWorkout(action.workoutId)
        }
    }

    private fun emitShowSankBarEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(
                WorkoutListUiEffect.ShowSnackbar(message)
            )
        }
    }

    private fun emitBackNavigation() {
        viewModelScope.launch {
            _uiEffect.emit(
                WorkoutListUiEffect.NavigationBack
            )
        }
    }

    private fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            try {
                deleteWorkoutUseCase(workoutId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emitShowSankBarEffect(e.message.toString())
            }

        }
    }
}


sealed class WorkoutListAction {
    object BackPress : WorkoutListAction()
    data class DeletePress(val workoutId: Long) : WorkoutListAction()
}

data class WorkoutListUiState(
    val isLoading: Boolean = true,
    val workouts: List<WorkoutItem> = emptyList(),
)

sealed class WorkoutListUiEffect {
    data class ShowSnackbar(val message: String) : WorkoutListUiEffect()
    object NavigationBack : WorkoutListUiEffect()
}
