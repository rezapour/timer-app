package me.rezapour.add_timer.viewmodel

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
import me.rezapour.domain.model.Routine
import me.rezapour.domain.usecase.InsertTimerUseCase

class AddTimerViewModel(private val insertUseCase: InsertTimerUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<AddTimerUiState> = MutableStateFlow(AddTimerUiState())
    val uiState: StateFlow<AddTimerUiState> = _uiState.asStateFlow()

    private val _uiEffect: MutableSharedFlow<AddTimerUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<AddTimerUiEffect> = _uiEffect.asSharedFlow()

    private fun saveTimer() {
        val state = uiState.value
        if (state.name.isBlank()) {
            showError("Name can't be empty")
            return
        }

        val routine = Routine(
            name = state.name,
            workSeconds = state.workoutSecond,
            restSeconds = state.restSecond,
            rounds = state.rounds
        )
        viewModelScope.launch {
            isSaving(true)
            try {
                insertUseCase(routine)
                _uiEffect.emit(AddTimerUiEffect.NavigationBack)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                showError(e.message.toString())
            } finally {
                isSaving(false)
            }


        }
    }

    fun onAction(event: AddTimerAction) {
        when (event) {
            AddTimerAction.SaveTimer -> saveTimer()
            is AddTimerAction.OnNameChanged -> onNameChange(event.name)
            AddTimerAction.OnRestDecreased -> restDecreaseValue()
            AddTimerAction.OnRestIncreased -> restIncreaseValue()
            AddTimerAction.OnRoundDecreased -> roundDecreaseValue()
            AddTimerAction.OnRoundIncreased -> roundIncreaseValue()
            AddTimerAction.OnWorkoutDecreased -> workoutDecreaseValue()
            AddTimerAction.OnWorkoutIncreased -> workoutIncreaseValue()
            AddTimerAction.BackClicked -> emitBackNavigation()
        }
    }

    private fun isSaving(isSaving: Boolean) {
        _uiState.update { it.copy(isSaving = isSaving) }
    }

    private fun workoutIncreaseValue() {
        _uiState.update {
            it.copy(workoutSecond = it.workoutSecond + 30)
        }
    }

    private fun workoutDecreaseValue() {
        _uiState.update {
            if (it.workoutSecond > AddTimerUiState.MIN_WORK_OUT)
                it.copy(workoutSecond = it.workoutSecond - 30)
            else
                it.copy(workoutSecond = AddTimerUiState.MIN_WORK_OUT)
        }
    }

    private fun restIncreaseValue() {
        _uiState.update {
            it.copy(restSecond = it.restSecond + 30)
        }
    }

    private fun restDecreaseValue() {
        _uiState.update {
            if (it.restSecond > AddTimerUiState.MIN_REST)
                it.copy(restSecond = it.restSecond - 30)
            else
                it.copy(restSecond = AddTimerUiState.MIN_REST)
        }
    }

    private fun roundIncreaseValue() {
        _uiState.update {
            it.copy(rounds = it.rounds + 1)
        }
    }

    private fun roundDecreaseValue() {
        _uiState.update {
            if (it.rounds > AddTimerUiState.MIN_ROUNDS)
                it.copy(rounds = it.rounds - 1)
            else
                it.copy(rounds = AddTimerUiState.MIN_ROUNDS)
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(AddTimerUiEffect.ShowSnackBar(message))
        }
    }

    private fun onNameChange(newNameValue: String) {
        _uiState.update { it.copy(name = newNameValue) }
    }

    private fun emitBackNavigation() {
        viewModelScope.launch {
            _uiEffect.emit(AddTimerUiEffect.NavigationBack)
        }
    }
}

sealed class AddTimerAction {

    object SaveTimer : AddTimerAction()
    data class OnNameChanged(val name: String) : AddTimerAction()
    object OnWorkoutIncreased : AddTimerAction()
    object OnWorkoutDecreased : AddTimerAction()
    object OnRestIncreased : AddTimerAction()
    object OnRestDecreased : AddTimerAction()
    object OnRoundIncreased : AddTimerAction()
    object OnRoundDecreased : AddTimerAction()
    object BackClicked : AddTimerAction()
}

data class AddTimerUiState(
    val isSaving: Boolean = false,
    val name: String = "",
    val workoutSecond: Long = MIN_WORK_OUT,
    val restSecond: Long = MIN_REST,
    val rounds: Int = MIN_ROUNDS,
) {
    companion object {
        const val MIN_REST = 30L
        const val MIN_WORK_OUT = 30L
        const val MIN_ROUNDS = 1
    }
}

sealed class AddTimerUiEffect {
    data class ShowSnackBar(val errorMessage: String) : AddTimerUiEffect()
    object NavigationBack : AddTimerUiEffect()

}