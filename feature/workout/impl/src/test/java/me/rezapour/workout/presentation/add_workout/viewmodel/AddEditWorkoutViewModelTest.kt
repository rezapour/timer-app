package me.rezapour.workout.presentation.add_workout.viewmodel

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
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
import me.rezapour.domain.usecase.GetWorkoutUseCase
import me.rezapour.domain.usecase.InsertWorkoutUseCase
import me.rezapour.domain.usecase.UpdateWorkoutUseCase
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

class AddEditWorkoutViewModelTest {

    private lateinit var addEditWorkoutViewModel: AddEditWorkoutViewModel

    @MockK(relaxed = true)
    private lateinit var insertWorkoutUseCase: InsertWorkoutUseCase

    @MockK(relaxed = true)
    private lateinit var getWorkoutUseCase: GetWorkoutUseCase

    @MockK(relaxed = true)
    private lateinit var updateWorkoutUseCase: UpdateWorkoutUseCase
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @Nested
    @DisplayName("Add Workout")
    inner class AddWorkout {

        @BeforeEach
        fun setupAddWorkoutTest() {
            addEditWorkoutViewModel = AddEditWorkoutViewModel(
                formMode = AddEditWorkoutFormMode.Add,
                insertWorkoutUseCase = insertWorkoutUseCase,
                getWorkoutUseCase = getWorkoutUseCase,
                updateWorkoutUseCase = updateWorkoutUseCase
            )
        }

        @Test
        fun `When viewmodel initializes with Add, state is Add and not loading`() =
            runTest(dispatcher) {
                val result = addEditWorkoutViewModel.uiState.value
                assertFalse(result.isLoading)
                assertEquals(AddEditWorkoutFormMode.Add, result.mode)
            }

        @Test
        fun `when save workout clicked, correct values in inserted`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            val expectedWorkout = 60L
            val expectedRest = 60L
            val expectedRounds = 2
            val expectedId = 0L

            addEditWorkoutViewModel.onAction(
                AddEditWorkoutAction.OnNameChanged(expectedName)
            )
            addEditWorkoutViewModel.onAction(
                AddEditWorkoutAction.WorkoutIncreased
            )
            addEditWorkoutViewModel.onAction(
                AddEditWorkoutAction.RestIncreased
            )
            addEditWorkoutViewModel.onAction(
                AddEditWorkoutAction.RoundIncreased
            )

            addEditWorkoutViewModel.onAction(
                AddEditWorkoutAction.SaveWorkout
            )

            advanceUntilIdle()

            val workoutSlot = slot<Workout>()

            coVerify(exactly = 1) {
                insertWorkoutUseCase(capture(workoutSlot))
            }

            coVerify(exactly = 0) {
                updateWorkoutUseCase(any())
            }

            val capturedWorkout = workoutSlot.captured

            assertEquals(expectedId, capturedWorkout.id)
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

                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                runCurrent()

                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                runCurrent()

                coVerify(exactly = 1) { insertWorkoutUseCase(any()) }

                insertGate.complete(Unit)
                advanceUntilIdle()
            }

        @Test
        fun `when workout is saved successfully, navigation back is emitted`() =
            runTest(dispatcher) {

                addEditWorkoutViewModel.uiEffect.test {
                    addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                    val effect = awaitItem()
                    assertInstanceOf(
                        AddEditWorkoutUiEffect.NavigationBack::class.java,
                        effect
                    )

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun `when workout insert fails, snackbar is emitted`() = runTest(dispatcher) {
            coEvery { insertWorkoutUseCase.invoke(any()) } throws IOException()

            addEditWorkoutViewModel.uiEffect.test {
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                val effect = awaitItem()
                assertInstanceOf(
                    AddEditWorkoutUiEffect.ShowSnackBar::class.java,
                    effect
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Edit Mode")
    inner class EditMode {

        private fun createViewModel() {
            addEditWorkoutViewModel = AddEditWorkoutViewModel(
                formMode = AddEditWorkoutFormMode.Edit(1),
                insertWorkoutUseCase = insertWorkoutUseCase,
                getWorkoutUseCase = getWorkoutUseCase,
                updateWorkoutUseCase = updateWorkoutUseCase
            )
        }

        @Test
        fun `When viewmodel initials with EditMode, state is Loading`() = runTest(dispatcher) {
            createViewModel()
            addEditWorkoutViewModel.uiState.test {
                assertTrue(awaitItem().isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        fun `when workout is loaded, state contains workout values and Edit mode`() =
            runTest(dispatcher) {
                val expectedName = "run"
                val expectedWork = 120L
                val expectedRest = 60L
                val expectedRound = 5
                val expectedMode = AddEditWorkoutFormMode.Edit(1)
                val expectedLoading = false
                coEvery { getWorkoutUseCase(1) } returns Workout(
                    name = expectedName,
                    workSeconds = expectedWork,
                    restSeconds = expectedRest,
                    rounds = expectedRound
                )

                createViewModel()
                advanceUntilIdle()
                val result = addEditWorkoutViewModel.uiState.value
                assertEquals(expectedMode, result.mode)
                assertEquals(expectedName, result.name)
                assertEquals(expectedWork, result.workoutSecond)
                assertEquals(expectedRest, result.restSecond)
                assertEquals(expectedRound, result.rounds)
                assertEquals(expectedLoading, result.isLoading)
            }

        @Test
        fun `when workout does not exist, state is Add and not loading`() = runTest(dispatcher) {
            coEvery { getWorkoutUseCase(1) } returns null
            createViewModel()

            advanceUntilIdle()

            val result = addEditWorkoutViewModel.uiState.value
            assertEquals(AddEditWorkoutFormMode.Add, result.mode)
            assertFalse(result.isLoading)
        }

        @Test
        fun `When workout doesn't exist, an error emits`() = runTest(dispatcher) {
            coEvery { getWorkoutUseCase(1) } returns null

            createViewModel()

            addEditWorkoutViewModel.uiEffect.test {
                assertInstanceOf(AddEditWorkoutUiEffect.ShowSnackBar::class.java, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        fun `when loading workout throw an exception, state is Add and not loading`() =
            runTest(dispatcher) {
                coEvery { getWorkoutUseCase(1) } throws IOException()
                createViewModel()

                advanceUntilIdle()

                val result = addEditWorkoutViewModel.uiState.value
                assertEquals(AddEditWorkoutFormMode.Add, result.mode)
                assertFalse(result.isLoading)
            }

        @Test
        fun `When loading workout throws an exception, an error emits`() = runTest(dispatcher) {
            coEvery { getWorkoutUseCase(1) } throws IOException()

            createViewModel()

            addEditWorkoutViewModel.uiEffect.test {
                assertInstanceOf(AddEditWorkoutUiEffect.ShowSnackBar::class.java, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        fun `When save is clicked multiple times during edit, workout is update once`() =
            runTest(dispatcher) {
                val deferred = CompletableDeferred<Unit>()
                coEvery { getWorkoutUseCase(1) } returns Workout(
                    name = "",
                    workSeconds = 30,
                    restSeconds = 30,
                    rounds = 1,
                )

                coEvery { updateWorkoutUseCase(any()) } coAnswers { deferred.await() }
                createViewModel()
                runCurrent()
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)
                runCurrent()

                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)
                runCurrent()

                coVerify(exactly = 1) { updateWorkoutUseCase(any()) }
                coVerify(exactly = 0) { insertWorkoutUseCase(any()) }


                deferred.complete(Unit)
                advanceUntilIdle()
            }

        @Test
        fun `When state is loading, save would be ignored`() = runTest(dispatcher) {
            val deferred = CompletableDeferred<Unit>()
            coEvery { getWorkoutUseCase(1) } coAnswers {
                deferred.await()
                Workout(
                    name = "",
                    workSeconds = 30,
                    restSeconds = 30,
                    rounds = 1,
                )
            }


            createViewModel()
            runCurrent()

            assertTrue(addEditWorkoutViewModel.uiState.value.isLoading)

            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)
            runCurrent()

            coVerify(exactly = 0) { updateWorkoutUseCase(any()) }
            coVerify(exactly = 0) { insertWorkoutUseCase(any()) }

            deferred.complete(Unit)
            advanceUntilIdle()
        }

        @Test
        fun `when workout is updated, use case receives correct values`() = runTest(dispatcher) {

            val expectedName = "run"
            val expectedWork = 130L
            val expectedRest = 60L
            val expectedRound = 6

            coEvery { getWorkoutUseCase(1) } returns Workout(
                name = "",
                workSeconds = 100,
                restSeconds = 30,
                rounds = 5
            )

            createViewModel()
            runCurrent()
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.OnNameChanged(expectedName))
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)
            advanceUntilIdle()
            val workout = slot<Workout>()

            coVerify(exactly = 1) { updateWorkoutUseCase(capture(workout)) }

            coVerify(exactly = 0) {
                insertWorkoutUseCase(any())
            }

            assertEquals(1L, workout.captured.id)
            assertEquals(expectedName, workout.captured.name)
            assertEquals(expectedWork, workout.captured.workSeconds)
            assertEquals(expectedRest, workout.captured.restSeconds)
            assertEquals(expectedRound, workout.captured.rounds)

        }

        @Test
        fun `when workout updated is successfully, navigation back is emitted`() =
            runTest(dispatcher) {

                coEvery { getWorkoutUseCase(1) } returns Workout(
                    name = "",
                    workSeconds = 100,
                    restSeconds = 30,
                    rounds = 5
                )

                createViewModel()
                runCurrent()
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.OnNameChanged("run"))
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                addEditWorkoutViewModel.uiEffect.test {

                    assertInstanceOf(AddEditWorkoutUiEffect.NavigationBack::class.java, awaitItem())
                }
            }

        @Test
        fun `when workout updated throws exception, showsnakBar is emitted`() =
            runTest(dispatcher) {

                coEvery { getWorkoutUseCase(1) } returns Workout(
                    name = "",
                    workSeconds = 100,
                    restSeconds = 30,
                    rounds = 5
                )
                coEvery { updateWorkoutUseCase(any()) } throws IOException()

                createViewModel()
                runCurrent()
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.OnNameChanged("run"))
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.SaveWorkout)

                addEditWorkoutViewModel.uiEffect.test {

                    assertInstanceOf(AddEditWorkoutUiEffect.ShowSnackBar::class.java, awaitItem())
                }
            }
    }


    @Nested
    @DisplayName("State Changes AddWorkout")
    inner class StateChanges {

        @BeforeEach
        fun setupAddWorkoutTest() {
            addEditWorkoutViewModel = AddEditWorkoutViewModel(
                formMode = AddEditWorkoutFormMode.Add,
                insertWorkoutUseCase = insertWorkoutUseCase,
                getWorkoutUseCase = getWorkoutUseCase,
                updateWorkoutUseCase = updateWorkoutUseCase
            )
        }

        @Test
        fun `when screen starts, decrease actions are disabled`() =
            runTest(dispatcher) {

                val result = addEditWorkoutViewModel.uiState.value
                assertFalse(result.workDecreasedEnabled)
                assertFalse(result.restDecreasedEnabled)
                assertFalse(result.roundDecreasedEnabled)
            }

        @Test
        fun `when name changes, state contains new name`() = runTest(dispatcher) {
            val expectedName = "Morning Run"
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.OnNameChanged(expectedName))

            val name = addEditWorkoutViewModel.uiState.value.name
            assertEquals(expectedName, name)
        }

        @Test
        fun `when workout increased, workout duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)

            val workout = addEditWorkoutViewModel.uiState.value.workoutSecond
            assertEquals(expectedValue, workout)
        }

        @Test
        fun `when workout rest, rest duration is updated `() = runTest(dispatcher) {
            val expectedValue = 60L
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)

            val rest = addEditWorkoutViewModel.uiState.value.restSecond
            assertEquals(expectedValue, rest)
        }

        @Test
        fun `when round increased once,Ui state update round them 1 time `() =
            runTest(dispatcher) {
                val expectedValue = 2
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)

                val round = addEditWorkoutViewModel.uiState.value.rounds
                assertEquals(expectedValue, round)
            }

        @Test
        fun `when workout reach min value, it return min value`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutDecreased)


            val workout = addEditWorkoutViewModel.uiState.value.workoutSecond
            assertEquals(AddEditWorkoutUiState.MIN_WORK_OUT, workout)
        }

        @Test
        fun `when rest reach min value, it return min value`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestDecreased)

            val rest = addEditWorkoutViewModel.uiState.value.restSecond
            assertEquals(AddEditWorkoutUiState.MIN_REST, rest)
        }

        @Test
        fun `when round reach min value, it return min value`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundDecreased)
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundDecreased)

            val round = addEditWorkoutViewModel.uiState.value.rounds
            assertEquals(AddEditWorkoutUiState.MIN_ROUNDS, round)
        }

        @Test
        fun `when workout is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)


            assertTrue(
                addEditWorkoutViewModel.uiState.value.workDecreasedEnabled
            )
        }

        @Test
        fun `when rest is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)


            assertTrue(
                addEditWorkoutViewModel.uiState.value.restDecreasedEnabled
            )
        }

        @Test
        fun `when rount is increased, workout decrease is enabled`() = runTest(dispatcher) {
            addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)

            assertTrue(
                addEditWorkoutViewModel.uiState.value.roundDecreasedEnabled
            )
        }


        @Test
        fun `when workout is decreased to minimum, workout decrease is disabled`() =
            runTest(dispatcher) {
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.WorkoutDecreased)


                assertFalse(
                    addEditWorkoutViewModel.uiState.value.workDecreasedEnabled
                )
            }

        @Test
        fun `when rest is decreased to minimum, rest decrease is disabled`() =
            runTest(dispatcher) {
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RestDecreased)

                assertFalse(
                    addEditWorkoutViewModel.uiState.value.restDecreasedEnabled
                )
            }

        @Test
        fun `when rounds are decreased to minimum, round decrease is disabled`() =
            runTest(dispatcher) {
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundIncreased)
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.RoundDecreased)

                assertFalse(
                    addEditWorkoutViewModel.uiState.value.roundDecreasedEnabled
                )
            }

    }

    @Nested
    @DisplayName("Navigation")
    inner class Navigation {

        @BeforeEach
        fun setupAddWorkoutTest() {
            addEditWorkoutViewModel = AddEditWorkoutViewModel(
                formMode = AddEditWorkoutFormMode.Add,
                insertWorkoutUseCase = insertWorkoutUseCase,
                getWorkoutUseCase = getWorkoutUseCase,
                updateWorkoutUseCase = updateWorkoutUseCase
            )
        }

        @Test
        fun `backClicked navigates emits navigateBack `() = runTest(dispatcher) {

            addEditWorkoutViewModel.uiEffect.test {
                addEditWorkoutViewModel.onAction(AddEditWorkoutAction.BackClicked)
                val effect = awaitItem()
                assertInstanceOf(
                    AddEditWorkoutUiEffect.NavigationBack::class.java,
                    effect
                )
            }
        }
    }
}
