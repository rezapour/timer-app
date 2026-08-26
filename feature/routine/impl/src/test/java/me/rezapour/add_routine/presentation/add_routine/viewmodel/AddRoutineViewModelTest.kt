package me.rezapour.add_routine.presentation.add_routine.viewmodel

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.rezapour.domain.model.Routine
import me.rezapour.domain.usecase.InsertRoutineUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException

class AddRoutineViewModelTest {

    private lateinit var addRoutineViewModel: AddRoutineViewModel
    private lateinit var insertRoutineUseCase: InsertRoutineUseCase
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        insertRoutineUseCase = mockk(relaxed = true)
        addRoutineViewModel = AddRoutineViewModel(insertRoutineUseCase)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @Nested
    @DisplayName("save Routine")
    inner class SaveRoutine {
        @Test
        fun `when save routine clicked, correct values in inserted`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            val expectedWorkout = 60L
            val expectedRest = 60L
            val expectedRounds = 2

            addRoutineViewModel.onAction(
                AddRoutineAction.OnNameChanged(expectedName)
            )
            addRoutineViewModel.onAction(
                AddRoutineAction.WorkoutIncreased
            )
            addRoutineViewModel.onAction(
                AddRoutineAction.RestIncreased
            )
            addRoutineViewModel.onAction(
                AddRoutineAction.RoundIncreased
            )

            addRoutineViewModel.onAction(
                AddRoutineAction.SaveRoutine
            )

            advanceUntilIdle()

            val routineSlot = slot<Routine>()

            coVerify(exactly = 1) {
                insertRoutineUseCase(capture(routineSlot))
            }

            val capturedRoutine = routineSlot.captured

            assertEquals(expectedName, capturedRoutine.name)
            assertEquals(expectedWorkout, capturedRoutine.workSeconds)
            assertEquals(expectedRest, capturedRoutine.restSeconds)
            assertEquals(expectedRounds, capturedRoutine.rounds)
        }

        @Test
        fun `when save is clicked multiple times during insert, routine is inserted once`() =
            runTest(dispatcher) {
                val insertGate = CompletableDeferred<Unit>()

                coEvery { insertRoutineUseCase(any()) } coAnswers {
                    insertGate.await()
                }

                addRoutineViewModel.onAction(AddRoutineAction.SaveRoutine)

                runCurrent()

                addRoutineViewModel.onAction(AddRoutineAction.SaveRoutine)

                runCurrent()

                coVerify(exactly = 1) { insertRoutineUseCase(any()) }

                insertGate.complete(Unit)
                advanceUntilIdle()
            }

        @Test
        fun `when routine is saved successfully, navigation back is emitted`() =
            runTest(dispatcher) {

                addRoutineViewModel.uiEffect.test {
                    addRoutineViewModel.onAction(AddRoutineAction.SaveRoutine)
                    advanceUntilIdle()
                    val effect = awaitItem()
                    assertInstanceOf(
                        AddRoutineUiEffect.NavigationBack::class.java,
                        effect
                    )

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun `when routine insert fails, snackbar is emitted`() = runTest(dispatcher) {
            coEvery { insertRoutineUseCase.invoke(any()) } throws IOException()

            addRoutineViewModel.uiEffect.test {
                addRoutineViewModel.onAction(AddRoutineAction.SaveRoutine)
                advanceUntilIdle()
                val effect = awaitItem()
                assertInstanceOf(
                    AddRoutineUiEffect.ShowSnackBar::class.java,
                    effect
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Nested
    @DisplayName("StateChanges")
    inner class StateChanges {
        @Test
        fun `when screen starts, decrease actions are disabled`() =
            runTest(dispatcher) {

                val result = addRoutineViewModel.uiState.value
                assertFalse(result.workDecreasedEnabled)
                assertFalse(result.restDecreasedEnabled)
                assertFalse(result.roundDecreasedEnabled)
            }

        @Test
        fun `when name changes, state contains new name`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            addRoutineViewModel.onAction(AddRoutineAction.OnNameChanged(expectedName))
            advanceUntilIdle()
            val name = addRoutineViewModel.uiState.value.name
            assertEquals(expectedName, name)
        }

        @Test
        fun `when workout increased, workout duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutIncreased)
            advanceUntilIdle()
            val workout = addRoutineViewModel.uiState.value.workoutSecond
            assertEquals(expectedValue, workout)
        }

        @Test
        fun `when workout rest, rest duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addRoutineViewModel.onAction(AddRoutineAction.RestIncreased)
            advanceUntilIdle()
            val rest = addRoutineViewModel.uiState.value.restSecond
            assertEquals(expectedValue, rest)
        }

        @Test
        fun `when round increased once,Ui state update round them 1 time `() = runTest(dispatcher) {
            val expectedValue = 2
            addRoutineViewModel.onAction(AddRoutineAction.RoundIncreased)
            advanceUntilIdle()
            val round = addRoutineViewModel.uiState.value.rounds
            assertEquals(expectedValue, round)
        }

        @Test
        fun `when workout reach min value, it return min value`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutDecreased)

            advanceUntilIdle()
            val workout = addRoutineViewModel.uiState.value.workoutSecond
            assertEquals(AddRoutineUiState.MIN_WORK_OUT, workout)
        }

        @Test
        fun `when rest reach min value, it return min value`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RestDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.RestDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.RestDecreased)

            advanceUntilIdle()
            val rest = addRoutineViewModel.uiState.value.restSecond
            assertEquals(AddRoutineUiState.MIN_REST, rest)
        }

        @Test
        fun `when round reach min value, it return min value`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RoundDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.RoundDecreased)
            addRoutineViewModel.onAction(AddRoutineAction.RoundDecreased)

            advanceUntilIdle()
            val round = addRoutineViewModel.uiState.value.rounds
            assertEquals(AddRoutineUiState.MIN_ROUNDS, round)
        }

        @Test
        fun `when workout is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutIncreased)

            advanceUntilIdle()

            assertTrue(
                addRoutineViewModel.uiState.value.workDecreasedEnabled
            )
        }

        @Test
        fun `when rest is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RestIncreased)

            advanceUntilIdle()

            assertTrue(
                addRoutineViewModel.uiState.value.restDecreasedEnabled
            )
        }

        @Test
        fun `when rount is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RoundIncreased)

            advanceUntilIdle()

            assertTrue(
                addRoutineViewModel.uiState.value.roundDecreasedEnabled
            )
        }


        @Test
        fun `when workout is decreased to minimum, workout decrease is disabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutIncreased)
            addRoutineViewModel.onAction(AddRoutineAction.WorkoutDecreased)

            advanceUntilIdle()

            assertFalse(
                addRoutineViewModel.uiState.value.workDecreasedEnabled
            )
        }

        @Test
        fun `when rest is decreased to minimum, rest decrease is disabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RestIncreased)
            addRoutineViewModel.onAction(AddRoutineAction.RestDecreased)

            advanceUntilIdle()

            assertFalse(
                addRoutineViewModel.uiState.value.restDecreasedEnabled
            )
        }

        @Test
        fun `when rounds are decreased to minimum, round decrease is disabled`() = runTest(dispatcher) {
            addRoutineViewModel.onAction(AddRoutineAction.RoundIncreased)
            addRoutineViewModel.onAction(AddRoutineAction.RoundDecreased)

            advanceUntilIdle()

            assertFalse(
                addRoutineViewModel.uiState.value.roundDecreasedEnabled
            )
        }

    }

    @Nested
    @DisplayName("Navigation")
    inner class Navigation {
        @Test
        fun `backClicked navigates emits navigateBack `() = runTest(dispatcher) {

            addRoutineViewModel.uiEffect.test {
                addRoutineViewModel.onAction(AddRoutineAction.BackClicked)
                advanceUntilIdle()
                val effect = awaitItem()
                assertInstanceOf(
                    AddRoutineUiEffect.NavigationBack::class.java,
                    effect
                )
            }
        }
    }
}