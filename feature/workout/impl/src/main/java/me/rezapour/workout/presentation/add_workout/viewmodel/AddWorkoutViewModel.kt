package me.rezapour.workout.presentation.add_workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import me.rezapour.domain.model.Workout
import me.rezapour.domain.usecase.InsertWorkoutUseCase

class AddWorkoutViewModel(private val insertUseCase: InsertWorkoutUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<AddWorkoutUiState> =
        MutableStateFlow(AddWorkoutUiState())
    val uiState: StateFlow<AddWorkoutUiState> = _uiState.asStateFlow()

    private val _uiEffect: MutableSharedFlow<AddWorkoutUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<AddWorkoutUiEffect> = _uiEffect.asSharedFlow()

    private val mutex = Mutex()

    private fun saveWorkout() {
        val state = uiState.value
        val workout = Workout(
            name = state.name.ifBlank { null },
            workSeconds = state.workoutSecond,
            restSeconds = state.restSecond,
            rounds = state.rounds
        )
        viewModelScope.launch {
            if (mutex.tryLock()) {
                try {
                    insertUseCase(workout)
                    _uiEffect.emit(AddWorkoutUiEffect.NavigationBack)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    showError(e.message.toString())
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    fun onAction(event: AddWorkoutAction) {
        when (event) {
            AddWorkoutAction.SaveWorkout -> saveWorkout()
            is AddWorkoutAction.OnNameChanged -> onNameChange(event.name)
            AddWorkoutAction.RestDecreased -> restDecreaseValue()
            AddWorkoutAction.RestIncreased -> restIncreaseValue()
            AddWorkoutAction.RoundDecreased -> roundDecreaseValue()
            AddWorkoutAction.RoundIncreased -> roundIncreaseValue()
            AddWorkoutAction.WorkoutDecreased -> workoutDecreaseValue()
            AddWorkoutAction.WorkoutIncreased -> workoutIncreaseValue()
            AddWorkoutAction.BackClicked -> emitBackNavigation()
        }
    }

    private fun workoutIncreaseValue() {
        _uiState.update {
            it.copy(
                workoutSecond = it.workoutSecond + 30
            )
        }
    }

    private fun workoutDecreaseValue() {
        _uiState.update {
            if (it.workoutSecond > AddWorkoutUiState.MIN_WORK_OUT)
                it.copy(workoutSecond = it.workoutSecond - 30)
            else
                it.copy(
                    workoutSecond = AddWorkoutUiState.MIN_WORK_OUT
                )
        }
    }

    private fun restIncreaseValue() {
        _uiState.update {
            it.copy(
                restSecond = it.restSecond + 30
            )
        }
    }

    private fun restDecreaseValue() {
        _uiState.update {
            if (it.restSecond > AddWorkoutUiState.MIN_REST)
                it.copy(restSecond = it.restSecond - 30)
            else
                it.copy(
                    restSecond = AddWorkoutUiState.MIN_REST
                )
        }
    }

    private fun roundIncreaseValue() {
        _uiState.update {
            it.copy(
                rounds = it.rounds + 1
            )
        }
    }

    private fun roundDecreaseValue() {
        _uiState.update {
            if (it.rounds > AddWorkoutUiState.MIN_ROUNDS)
                it.copy(rounds = it.rounds - 1)
            else
                it.copy(
                    rounds = AddWorkoutUiState.MIN_ROUNDS
                )
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(AddWorkoutUiEffect.ShowSnackBar(message))
        }
    }

    private fun onNameChange(newNameValue: String) {
        _uiState.update { it.copy(name = newNameValue) }
    }

    private fun emitBackNavigation() {
        viewModelScope.launch {
            _uiEffect.emit(AddWorkoutUiEffect.NavigationBack)
        }
    }
}

sealed class AddWorkoutAction {

    object SaveWorkout : AddWorkoutAction()
    data class OnNameChanged(val name: String) : AddWorkoutAction()
    object WorkoutIncreased : AddWorkoutAction()
    object WorkoutDecreased : AddWorkoutAction()
    object RestIncreased : AddWorkoutAction()
    object RestDecreased : AddWorkoutAction()
    object RoundIncreased : AddWorkoutAction()
    object RoundDecreased : AddWorkoutAction()
    object BackClicked : AddWorkoutAction()
}

data class AddWorkoutUiState(
    val name: String = "",
    val workoutSecond: Long = MIN_WORK_OUT,
    val restSecond: Long = MIN_REST,
    val rounds: Int = MIN_ROUNDS,
) {
    val total = workoutSecond * rounds + restSecond * (rounds - 1)
    val workDecreasedEnabled: Boolean
        get() = workoutSecond > MIN_WORK_OUT

    val restDecreasedEnabled: Boolean
        get() = restSecond > MIN_REST

    val roundDecreasedEnabled: Boolean
        get() = rounds > MIN_ROUNDS


    companion object {
        const val MIN_REST = 30L
        const val MIN_WORK_OUT = 30L
        const val MIN_ROUNDS = 1
    }
}

sealed class AddWorkoutUiEffect {
    data class ShowSnackBar(val errorMessage: String) : AddWorkoutUiEffect()
    object NavigationBack : AddWorkoutUiEffect()

}
