package me.rezapour.add_timer.presentation.viewmodel

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
import me.rezapour.domain.model.Routine
import me.rezapour.domain.usecase.InsertTimerUseCase

class AddRoutineViewModel(private val insertUseCase: InsertTimerUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<AddRoutineUiState> =
        MutableStateFlow(AddRoutineUiState())
    val uiState: StateFlow<AddRoutineUiState> = _uiState.asStateFlow()

    private val _uiEffect: MutableSharedFlow<AddRoutineUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<AddRoutineUiEffect> = _uiEffect.asSharedFlow()

    private val mutex = Mutex()

    private fun saveRoutine() {
        val state = uiState.value
        val routine = Routine(
            name = state.name.ifBlank { null },
            workSeconds = state.workoutSecond,
            restSeconds = state.restSecond,
            rounds = state.rounds
        )
        viewModelScope.launch {
            if (mutex.tryLock()) {
                try {
                    insertUseCase(routine)
                    _uiEffect.emit(AddRoutineUiEffect.NavigationBack)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    showError(e.message.toString())
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    fun onAction(event: AddRoutineAction) {
        when (event) {
            AddRoutineAction.SaveRoutine -> saveRoutine()
            is AddRoutineAction.OnNameChanged -> onNameChange(event.name)
            AddRoutineAction.RestDecreased -> restDecreaseValue()
            AddRoutineAction.RestIncreased -> restIncreaseValue()
            AddRoutineAction.RoundDecreased -> roundDecreaseValue()
            AddRoutineAction.RoundIncreased -> roundIncreaseValue()
            AddRoutineAction.WorkoutDecreased -> workoutDecreaseValue()
            AddRoutineAction.WorkoutIncreased -> workoutIncreaseValue()
            AddRoutineAction.BackClicked -> emitBackNavigation()
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
            if (it.workoutSecond > AddRoutineUiState.MIN_WORK_OUT)
                it.copy(workoutSecond = it.workoutSecond - 30)
            else
                it.copy(
                    workoutSecond = AddRoutineUiState.MIN_WORK_OUT
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
            if (it.restSecond > AddRoutineUiState.MIN_REST)
                it.copy(restSecond = it.restSecond - 30)
            else
                it.copy(
                    restSecond = AddRoutineUiState.MIN_REST
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
            if (it.rounds > AddRoutineUiState.MIN_ROUNDS)
                it.copy(rounds = it.rounds - 1)
            else
                it.copy(
                    rounds = AddRoutineUiState.MIN_ROUNDS
                )
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(AddRoutineUiEffect.ShowSnackBar(message))
        }
    }

    private fun onNameChange(newNameValue: String) {
        _uiState.update { it.copy(name = newNameValue) }
    }

    private fun emitBackNavigation() {
        viewModelScope.launch {
            _uiEffect.emit(AddRoutineUiEffect.NavigationBack)
        }
    }
}

sealed class AddRoutineAction {

    object SaveRoutine : AddRoutineAction()
    data class OnNameChanged(val name: String) : AddRoutineAction()
    object WorkoutIncreased : AddRoutineAction()
    object WorkoutDecreased : AddRoutineAction()
    object RestIncreased : AddRoutineAction()
    object RestDecreased : AddRoutineAction()
    object RoundIncreased : AddRoutineAction()
    object RoundDecreased : AddRoutineAction()
    object BackClicked : AddRoutineAction()
}

data class AddRoutineUiState(
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

sealed class AddRoutineUiEffect {
    data class ShowSnackBar(val errorMessage: String) : AddRoutineUiEffect()
    object NavigationBack : AddRoutineUiEffect()

}