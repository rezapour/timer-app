package me.rezapour.workout.presentation.my_workouts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.rezapour.domain.model.Workout
import me.rezapour.domain.usecase.GetWorkoutsUseCase
import me.rezapour.ui.mapper.Mapper
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem

class MyWorkoutsViewmodel(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val mapper: Mapper<Workout, WorkoutItem>
) : ViewModel() {

    private val _uiEffect: MutableSharedFlow<MyWorkoutsUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<MyWorkoutsUiEffect> = _uiEffect.asSharedFlow()

    val uiState: StateFlow<MyWorkoutsUiState> = getWorkoutsUseCase()
        .map { workouts ->
            MyWorkoutsUiState(
                workouts = mapper.mapDomainToUIModel(workouts),
                isLoading = false,
                errorMessage = null
            )
        }
        .catch { error ->
            emit(MyWorkoutsUiState(errorMessage = error.message, isLoading = false))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyWorkoutsUiState()
        )


    fun onAction(action: MyWorkoutsAction) {
        when (action) {
            is MyWorkoutsAction.PlayClicked -> emitStartWorkout(action.workout.id)
            MyWorkoutsAction.AddWorkoutClicked -> navigateToAddWorkout()
            is MyWorkoutsAction.RowClicked -> navigateToEditWorkout(action.workout.id)
        }
    }

    private fun emitShowSankBarEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(
                MyWorkoutsUiEffect.ShowSnackbar(message)
            )
        }
    }

    private fun navigateToAddWorkout() {
        viewModelScope.launch {
            _uiEffect.emit(MyWorkoutsUiEffect.NavigateAddWorkout)
        }
    }

    private fun navigateToEditWorkout(workoutId: Long) {
        viewModelScope.launch {
            _uiEffect.emit(MyWorkoutsUiEffect.NavigateEditeWorkout(workoutId))
        }
    }

    private fun emitStartWorkout(workoutId: Long) {
        viewModelScope.launch {
            _uiEffect.emit(MyWorkoutsUiEffect.StartWorkout(workoutId))
        }
    }
}


sealed class MyWorkoutsAction {
    data class PlayClicked(val workout: WorkoutItem) : MyWorkoutsAction()
    data class RowClicked(val workout: WorkoutItem) : MyWorkoutsAction()
    data object AddWorkoutClicked : MyWorkoutsAction()
}

data class MyWorkoutsUiState(
    val isLoading: Boolean = true,
    val workouts: List<WorkoutItem> = emptyList(),
    val errorMessage: String? = null
)

sealed class MyWorkoutsUiEffect {
    data class ShowSnackbar(val message: String) : MyWorkoutsUiEffect()
    data class StartWorkout(val workoutId: Long) : MyWorkoutsUiEffect()
    data class NavigateEditeWorkout(val workoutId: Long) : MyWorkoutsUiEffect()
    data object NavigateAddWorkout : MyWorkoutsUiEffect()
}
