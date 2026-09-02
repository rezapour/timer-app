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
import me.rezapour.domain.usecase.DeleteWorkoutUseCase
import me.rezapour.domain.usecase.GetWorkoutUseCase
import me.rezapour.domain.usecase.InsertWorkoutUseCase
import me.rezapour.domain.usecase.UpdateWorkoutUseCase

class AddEditWorkoutViewModel(
    formMode: AddEditWorkoutFormMode,
    private val insertWorkoutUseCase: InsertWorkoutUseCase,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddEditWorkoutUiState> =
        MutableStateFlow(
            AddEditWorkoutUiState(
                isLoading = formMode is AddEditWorkoutFormMode.Edit
            )
        )
    val uiState: StateFlow<AddEditWorkoutUiState> = _uiState.asStateFlow()

    private val _uiEffect: MutableSharedFlow<AddEditWorkoutUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<AddEditWorkoutUiEffect> = _uiEffect.asSharedFlow()

    private val mutex = Mutex()

    fun onAction(event: AddEditWorkoutAction) {
        when (event) {
            AddEditWorkoutAction.SaveWorkout -> saveWorkout()
            is AddEditWorkoutAction.OnNameChanged -> onNameChange(event.name)
            AddEditWorkoutAction.RestDecreased -> restDecreaseValue()
            AddEditWorkoutAction.RestIncreased -> restIncreaseValue()
            AddEditWorkoutAction.RoundDecreased -> roundDecreaseValue()
            AddEditWorkoutAction.RoundIncreased -> roundIncreaseValue()
            AddEditWorkoutAction.WorkoutDecreased -> workoutDecreaseValue()
            AddEditWorkoutAction.WorkoutIncreased -> workoutIncreaseValue()
            AddEditWorkoutAction.BackClicked -> navigationBack()
            AddEditWorkoutAction.DeleteWorkout -> deleteWorkout()
        }
    }

    init {
        when (formMode) {
            AddEditWorkoutFormMode.Add -> Unit
            is AddEditWorkoutFormMode.Edit -> loadData(formMode.workoutId)
        }
    }

    private fun loadData(workoutId: Long) {
        viewModelScope.launch {
            try {
                val workout = getWorkoutUseCase(workoutId)

                if (workout == null) {
                    showError("Workout not found")
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        mode = AddEditWorkoutFormMode.Edit(workoutId),
                        name = workout.name ?: "",
                        workoutSecond = workout.workSeconds,
                        restSecond = workout.restSeconds,
                        rounds = workout.rounds,
                        isLoading = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                showError(e.message.toString())
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun saveWorkout() {
        viewModelScope.launch {
            if (uiState.value.isLoading) return@launch
            if (!mutex.tryLock()) return@launch

            try {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                val state = uiState.value
                val mode = state.mode

                val workout = Workout(
                    id = when (mode) {
                        AddEditWorkoutFormMode.Add -> 0L
                        is AddEditWorkoutFormMode.Edit -> mode.workoutId
                    },
                    name = state.name.ifBlank { null },
                    workSeconds = state.workoutSecond,
                    restSeconds = state.restSecond,
                    rounds = state.rounds
                )

                when (mode) {
                    AddEditWorkoutFormMode.Add -> insertWorkoutUseCase(workout)
                    is AddEditWorkoutFormMode.Edit -> updateWorkoutUseCase(workout)
                }

                _uiEffect.emit(AddEditWorkoutUiEffect.NavigationBack)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                showError(e.message.orEmpty())
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
                mutex.unlock()
            }
        }
    }

    private fun deleteWorkout() {
        viewModelScope.launch {
            if (uiState.value.isLoading) return@launch

            val modeForm = uiState.value.mode
            if (modeForm !is AddEditWorkoutFormMode.Edit) return@launch

            if (!mutex.tryLock()) return@launch
            try {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                deleteWorkoutUseCase(modeForm.workoutId)
                emitBackNavigation()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                showError(e.message.orEmpty())
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
                mutex.unlock()
            }
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
            if (it.workoutSecond > AddEditWorkoutUiState.MIN_WORK_OUT)
                it.copy(workoutSecond = it.workoutSecond - 30)
            else
                it.copy(
                    workoutSecond = AddEditWorkoutUiState.MIN_WORK_OUT
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
            if (it.restSecond > AddEditWorkoutUiState.MIN_REST)
                it.copy(restSecond = it.restSecond - 30)
            else
                it.copy(
                    restSecond = AddEditWorkoutUiState.MIN_REST
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
            if (it.rounds > AddEditWorkoutUiState.MIN_ROUNDS)
                it.copy(rounds = it.rounds - 1)
            else
                it.copy(
                    rounds = AddEditWorkoutUiState.MIN_ROUNDS
                )
        }
    }

    private suspend fun showError(message: String) {
        _uiEffect.emit(AddEditWorkoutUiEffect.ShowSnackBar(message))
    }

    private fun onNameChange(newNameValue: String) {
        _uiState.update { it.copy(name = newNameValue) }
    }

    private suspend fun emitBackNavigation() {
        _uiEffect.emit(AddEditWorkoutUiEffect.NavigationBack)
    }

    private fun navigationBack() {
        viewModelScope.launch {
            _uiEffect.emit(AddEditWorkoutUiEffect.NavigationBack)
        }
    }
}


sealed class AddEditWorkoutAction {

    object SaveWorkout : AddEditWorkoutAction()
    data class OnNameChanged(val name: String) : AddEditWorkoutAction()
    object WorkoutIncreased : AddEditWorkoutAction()
    object WorkoutDecreased : AddEditWorkoutAction()
    object RestIncreased : AddEditWorkoutAction()
    object RestDecreased : AddEditWorkoutAction()
    object RoundIncreased : AddEditWorkoutAction()
    object RoundDecreased : AddEditWorkoutAction()
    object BackClicked : AddEditWorkoutAction()
    data object DeleteWorkout : AddEditWorkoutAction()
}

data class AddEditWorkoutUiState(
    val name: String = "",
    val workoutSecond: Long = MIN_WORK_OUT,
    val restSecond: Long = MIN_REST,
    val rounds: Int = MIN_ROUNDS,
    val mode: AddEditWorkoutFormMode = AddEditWorkoutFormMode.Add,
    val isLoading: Boolean = false
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

sealed class AddEditWorkoutUiEffect {
    data class ShowSnackBar(val errorMessage: String) : AddEditWorkoutUiEffect()
    object NavigationBack : AddEditWorkoutUiEffect()

}

sealed interface AddEditWorkoutFormMode {
    data object Add : AddEditWorkoutFormMode
    data class Edit(val workoutId: Long) : AddEditWorkoutFormMode
}
