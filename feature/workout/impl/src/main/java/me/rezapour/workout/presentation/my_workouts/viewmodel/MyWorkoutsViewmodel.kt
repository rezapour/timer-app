package me.rezapour.workout.presentation.my_workouts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    private val retryState = MutableSharedFlow<Unit>()
    val uiState: StateFlow<MyWorkoutsUiState> = retryState.onStart { emit(Unit) }
        .flatMapLatest {
            getWorkoutsUseCase()
                .map<List<Workout>, MyWorkoutsUiState> { workouts ->
                    MyWorkoutsUiState.Success(
                        workouts = mapper.mapDomainToUIModel(workouts),
                    )
                }
                .onStart { emit(MyWorkoutsUiState.Loading) }
                .catch { error ->
                    emit(MyWorkoutsUiState.Error(errorMessage = error.message.toString()))
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyWorkoutsUiState.Loading
        )


    fun onAction(action: MyWorkoutsAction) {
        when (action) {
            is MyWorkoutsAction.PlayClicked -> emitStartWorkout(action.workout.id)
            MyWorkoutsAction.AddWorkoutClicked -> navigateToAddWorkout()
            is MyWorkoutsAction.RowClicked -> navigateToEditWorkout(action.workout.id)
            MyWorkoutsAction.RetryClicked -> retry()
        }
    }

    private fun retry() {
        viewModelScope.launch {
            retryState.emit(Unit)
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
            _uiEffect.emit(MyWorkoutsUiEffect.NavigateEditWorkout(workoutId))
        }
    }

    private fun emitStartWorkout(workoutId: Long) {
        viewModelScope.launch {
            _uiEffect.emit(MyWorkoutsUiEffect.StartWorkout(workoutId))
        }
    }
}


sealed interface MyWorkoutsAction {
    data class PlayClicked(val workout: WorkoutItem) : MyWorkoutsAction
    data class RowClicked(val workout: WorkoutItem) : MyWorkoutsAction
    data object AddWorkoutClicked : MyWorkoutsAction
    data object RetryClicked : MyWorkoutsAction
}

sealed interface MyWorkoutsUiState {

    data class Success(val workouts: List<WorkoutItem> = emptyList()) : MyWorkoutsUiState
    data object Loading : MyWorkoutsUiState
    data class Error(val errorMessage: String? = null) : MyWorkoutsUiState

}

sealed class MyWorkoutsUiEffect {
    data class ShowSnackbar(val message: String) : MyWorkoutsUiEffect()
    data class StartWorkout(val workoutId: Long) : MyWorkoutsUiEffect()
    data class NavigateEditWorkout(val workoutId: Long) : MyWorkoutsUiEffect()
    data object NavigateAddWorkout : MyWorkoutsUiEffect()
}
