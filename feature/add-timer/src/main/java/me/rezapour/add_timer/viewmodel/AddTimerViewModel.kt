package me.rezapour.add_timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rezapour.common.extentionfunctions.digitOnly
import me.rezapour.common.extentionfunctions.toIntOrZero
import me.rezapour.common.extentionfunctions.toLongOrZero
import me.rezapour.domain.model.Timer
import me.rezapour.domain.usecase.InsertTimerUseCase

class AddTimerViewModel(private val insertUseCase: InsertTimerUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<AddTimerUiState> = MutableStateFlow(AddTimerUiState())
    val uiState: StateFlow<AddTimerUiState> = _uiState.asStateFlow()

    fun saveTimer() {
        val state = uiState.value
        if (state.name == null) {
            showError("Name can't be empty")
            return
        }
        val workSeconds = minuteSecondsToTotalSeconds(state.workMin, state.workSec)
        val restSeconds = minuteSecondsToTotalSeconds(state.restMin, state.restSec)

        val timer = Timer(
            name = state.name,
            workSeconds = workSeconds,
            restSeconds = restSeconds,
            rounds = state.rounds.toIntOrZero()
        )
        viewModelScope.launch {
            insertUseCase(timer)
        }
    }

    fun updateUiEvent(e: AddTimerUiEvent) {
        when (e) {
            is AddTimerUiEvent.OnRestMinChanged -> onIntervalMinChanged(e.min)
            is AddTimerUiEvent.OnRestSecChanged -> onIntervalSecChange(e.sec)
            AddTimerUiEvent.SaveTimer -> saveTimer()
            is AddTimerUiEvent.OnNameChanged -> onNameChange(e.name)
            is AddTimerUiEvent.OnRoundsChanged -> onSetChange(e.set)
            is AddTimerUiEvent.OnWorkMinChanged -> onTimerMinChange(e.min)
            is AddTimerUiEvent.OnWorkSecChanged -> onTimerSecChange(e.sec)
        }
    }

    private fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun onNameChange(newNameValue: String) {
        _uiState.update { it.copy(name = newNameValue, errorMessage = null) }
    }

    private fun onTimerMinChange(newMin: String) {
        _uiState.update { it.copy(workMin = validateTimeInput(newMin)) }
    }

    private fun onTimerSecChange(newSec: String) {
        _uiState.update { it.copy(workSec = validateTimeInput(newSec)) }
    }

    private fun onIntervalMinChanged(newMin: String) {
        _uiState.update { it.copy(restMin = validateTimeInput(newMin)) }
    }

    private fun onIntervalSecChange(newSec: String) {
        _uiState.update { it.copy(restSec = validateTimeInput(newSec)) }
    }

    private fun onSetChange(newSet: String) {
        _uiState.update { it.copy(rounds = newSet.digitOnly()) }
    }

    private fun validateTimeInput(value: String) =
        value.digitOnly(2).toIntOrZero().coerceIn(0, 59).toString()

    private fun minuteSecondsToTotalSeconds(minText: String, secText: String): Long {
        val min = minText.toLongOrZero() * 60L
        val sec = secText.toLongOrZero()
        return min + sec
    }
}

sealed class AddTimerUiEvent {

    object SaveTimer : AddTimerUiEvent()
    data class OnNameChanged(val name: String) : AddTimerUiEvent()
    data class OnWorkMinChanged(val min: String) : AddTimerUiEvent()
    data class OnWorkSecChanged(val sec: String) : AddTimerUiEvent()
    data class OnRestMinChanged(val min: String) : AddTimerUiEvent()
    data class OnRestSecChanged(val sec: String) : AddTimerUiEvent()
    data class OnRoundsChanged(val set: String) : AddTimerUiEvent()
}

data class AddTimerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val name: String? = null,
    val workMin: String = "",
    val workSec: String = "",
    val restMin: String = "",
    val restSec: String = "",
    val rounds: String = ""
)