package me.rezapour.timer_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import me.rezapour.domain.model.Timer
import me.rezapour.domain.usecase.DeleteTimerUseCase
import me.rezapour.domain.usecase.GetTimersUseCase
import me.rezapour.timer_list.model.TimerItem
import me.rezapour.ui.mapper.Mapper

class TimerListViewModel(
    private val getTimersUseCase: GetTimersUseCase,
    private val deleteTimersUseCase: DeleteTimerUseCase,
    private val mapper: Mapper<Timer, TimerItem>
) : ViewModel() {

    private val _uiEffect: MutableSharedFlow<TimerListUiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<TimerListUiEffect> = _uiEffect.asSharedFlow()

    val uiState: StateFlow<TimerListUiState> = getTimersUseCase()
        .map { timers ->
            TimerListUiState(
                timers = mapper.mapDomainToUIModel(timers)
            )
        }
        .onStart { emit(TimerListUiState(isLoading = true)) }
        .catch { error ->
            emit(TimerListUiState(isLoading = false))
            emitShowSankBarEffect(error.message.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerListUiState()
        )


    fun onAction(action: TimerListAction) {
        when (action) {
            TimerListAction.BackPress -> emitBackNavigation()
            is TimerListAction.DeletePress -> deleteTimer(action.timerId)
        }
    }

    private fun emitShowSankBarEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.emit(
                TimerListUiEffect.ShowSnackbar(message)
            )
        }
    }

    private fun emitBackNavigation() {
        viewModelScope.launch {
            _uiEffect.emit(
                TimerListUiEffect.NavigationBack
            )
        }
    }

    private fun deleteTimer(timerId: Long) {
        viewModelScope.launch {
            deleteTimersUseCase(timerId)
        }
    }
}


sealed class TimerListAction {
    object BackPress : TimerListAction()
    data class DeletePress(val timerId: Long) : TimerListAction()
}

data class TimerListUiState(
    val isLoading: Boolean = true,
    val timers: List<TimerItem> = emptyList(),
)

sealed class TimerListUiEffect {
    data class ShowSnackbar(val message: String) : TimerListUiEffect()
    object NavigationBack : TimerListUiEffect()
}