package me.rezapour.workout.presentation.add_workout.viewmodel

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
import me.rezapour.domain.model.Workout
import me.rezapour.domain.usecase.InsertWorkoutUseCase
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

class AddWorkoutViewModelTest {

    private lateinit var addWorkoutViewModel: AddWorkoutViewModel
    private lateinit var insertWorkoutUseCase: InsertWorkoutUseCase
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        insertWorkoutUseCase = mockk(relaxed = true)
        addWorkoutViewModel = AddWorkoutViewModel(insertWorkoutUseCase)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @Nested
    @DisplayName("save Workout")
    inner class SaveWorkout {
        @Test
        fun `when save workout clicked, correct values in inserted`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            val expectedWorkout = 60L
            val expectedRest = 60L
            val expectedRounds = 2

            addWorkoutViewModel.onAction(
                AddWorkoutAction.OnNameChanged(expectedName)
            )
            addWorkoutViewModel.onAction(
                AddWorkoutAction.WorkoutIncreased
            )
            addWorkoutViewModel.onAction(
                AddWorkoutAction.RestIncreased
            )
            addWorkoutViewModel.onAction(
                AddWorkoutAction.RoundIncreased
            )

            addWorkoutViewModel.onAction(
                AddWorkoutAction.SaveWorkout
            )

            advanceUntilIdle()

            val workoutSlot = slot<Workout>()

            coVerify(exactly = 1) {
                insertWorkoutUseCase(capture(workoutSlot))
            }

            val capturedWorkout = workoutSlot.captured

            assertEquals(expectedName, capturedWorkout.name)
            assertEquals(expectedWorkout, capturedWorkout.workSeconds)
            assertEquals(expectedRest, capturedWorkout.restSeconds)
            assertEquals(expectedRounds, capturedWorkout.rounds)
        }

        @Test
        fun `when save is clicked multiple times during insert, workout is inserted once`() =
            runTest(dispatcher) {
                val insertGate = CompletableDeferred<Unit>()

                coEvery { insertWorkoutUseCase(any()) } coAnswers {
                    insertGate.await()
                }

                addWorkoutViewModel.onAction(AddWorkoutAction.SaveWorkout)

                runCurrent()

                addWorkoutViewModel.onAction(AddWorkoutAction.SaveWorkout)

                runCurrent()

                coVerify(exactly = 1) { insertWorkoutUseCase(any()) }

                insertGate.complete(Unit)
                advanceUntilIdle()
            }

        @Test
        fun `when workout is saved successfully, navigation back is emitted`() =
            runTest(dispatcher) {

                addWorkoutViewModel.uiEffect.test {
                    addWorkoutViewModel.onAction(AddWorkoutAction.SaveWorkout)
                    advanceUntilIdle()
                    val effect = awaitItem()
                    assertInstanceOf(
                        AddWorkoutUiEffect.NavigationBack::class.java,
                        effect
                    )

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun `when workout insert fails, snackbar is emitted`() = runTest(dispatcher) {
            coEvery { insertWorkoutUseCase.invoke(any()) } throws IOException()

            addWorkoutViewModel.uiEffect.test {
                addWorkoutViewModel.onAction(AddWorkoutAction.SaveWorkout)
                advanceUntilIdle()
                val effect = awaitItem()
                assertInstanceOf(
                    AddWorkoutUiEffect.ShowSnackBar::class.java,
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

                val result = addWorkoutViewModel.uiState.value
                assertFalse(result.workDecreasedEnabled)
                assertFalse(result.restDecreasedEnabled)
                assertFalse(result.roundDecreasedEnabled)
            }

        @Test
        fun `when name changes, state contains new name`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            addWorkoutViewModel.onAction(AddWorkoutAction.OnNameChanged(expectedName))
            advanceUntilIdle()
            val name = addWorkoutViewModel.uiState.value.name
            assertEquals(expectedName, name)
        }

        @Test
        fun `when workout increased, workout duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutIncreased)
            advanceUntilIdle()
            val workout = addWorkoutViewModel.uiState.value.workoutSecond
            assertEquals(expectedValue, workout)
        }

        @Test
        fun `when workout rest, rest duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addWorkoutViewModel.onAction(AddWorkoutAction.RestIncreased)
            advanceUntilIdle()
            val rest = addWorkoutViewModel.uiState.value.restSecond
            assertEquals(expectedValue, rest)
        }

        @Test
        fun `when round increased once,Ui state update round them 1 time `() = runTest(dispatcher) {
            val expectedValue = 2
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundIncreased)
            advanceUntilIdle()
            val round = addWorkoutViewModel.uiState.value.rounds
            assertEquals(expectedValue, round)
        }

        @Test
        fun `when workout reach min value, it return min value`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutDecreased)

            advanceUntilIdle()
            val workout = addWorkoutViewModel.uiState.value.workoutSecond
            assertEquals(AddWorkoutUiState.MIN_WORK_OUT, workout)
        }

        @Test
        fun `when rest reach min value, it return min value`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RestDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RestDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RestDecreased)

            advanceUntilIdle()
            val rest = addWorkoutViewModel.uiState.value.restSecond
            assertEquals(AddWorkoutUiState.MIN_REST, rest)
        }

        @Test
        fun `when round reach min value, it return min value`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundDecreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundDecreased)

            advanceUntilIdle()
            val round = addWorkoutViewModel.uiState.value.rounds
            assertEquals(AddWorkoutUiState.MIN_ROUNDS, round)
        }

        @Test
        fun `when workout is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutIncreased)

            advanceUntilIdle()

            assertTrue(
                addWorkoutViewModel.uiState.value.workDecreasedEnabled
            )
        }

        @Test
        fun `when rest is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RestIncreased)

            advanceUntilIdle()

            assertTrue(
                addWorkoutViewModel.uiState.value.restDecreasedEnabled
            )
        }

        @Test
        fun `when rount is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundIncreased)

            advanceUntilIdle()

            assertTrue(
                addWorkoutViewModel.uiState.value.roundDecreasedEnabled
            )
        }


        @Test
        fun `when workout is decreased to minimum, workout decrease is disabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutIncreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.WorkoutDecreased)

            advanceUntilIdle()

            assertFalse(
                addWorkoutViewModel.uiState.value.workDecreasedEnabled
            )
        }

        @Test
        fun `when rest is decreased to minimum, rest decrease is disabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RestIncreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RestDecreased)

            advanceUntilIdle()

            assertFalse(
                addWorkoutViewModel.uiState.value.restDecreasedEnabled
            )
        }

        @Test
        fun `when rounds are decreased to minimum, round decrease is disabled`() = runTest(dispatcher) {
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundIncreased)
            addWorkoutViewModel.onAction(AddWorkoutAction.RoundDecreased)

            advanceUntilIdle()

            assertFalse(
                addWorkoutViewModel.uiState.value.roundDecreasedEnabled
            )
        }

    }

    @Nested
    @DisplayName("Navigation")
    inner class Navigation {
        @Test
        fun `backClicked navigates emits navigateBack `() = runTest(dispatcher) {

            addWorkoutViewModel.uiEffect.test {
                addWorkoutViewModel.onAction(AddWorkoutAction.BackClicked)
                advanceUntilIdle()
                val effect = awaitItem()
                assertInstanceOf(
                    AddWorkoutUiEffect.NavigationBack::class.java,
                    effect
                )
            }
        }
    }
}
