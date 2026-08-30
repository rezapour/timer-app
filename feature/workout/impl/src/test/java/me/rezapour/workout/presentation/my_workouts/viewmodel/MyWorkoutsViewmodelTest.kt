package me.rezapour.workout.presentation.my_workouts.viewmodel

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.rezapour.domain.model.Workout
import me.rezapour.domain.usecase.GetWorkoutsUseCase
import me.rezapour.workout.presentation.fake.WorkoutStubWithOneItem
import me.rezapour.workout.presentation.fake.WorkoutStubWithTwoItems
import me.rezapour.workout.presentation.my_workouts.mapper.WorkoutItemMapper
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException

class MyWorkoutsViewmodelTest {

    private lateinit var viewmodel: MyWorkoutsViewmodel
    private lateinit var getWorkoutsUseCase: GetWorkoutsUseCase
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        getWorkoutsUseCase = mockk()
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Get Workouts")
    inner class GetWorkOuts {
        @Test
        fun `When There is no workout, UiState workouts is empty`() = runTest(dispatcher) {
            every { getWorkoutsUseCase.invoke() } returns flow { emit(emptyList<Workout>()) }

            viewmodel = MyWorkoutsViewmodel(
                getWorkoutsUseCase = getWorkoutsUseCase,
                mapper = WorkoutItemMapper()
            )

            viewmodel.uiState.test {
                assertInstanceOf(
                    MyWorkoutsUiState.Loading::class.java, awaitItem()
                )
                val state = assertInstanceOf(
                    MyWorkoutsUiState.Success::class.java, awaitItem()
                )

                assertEquals(emptyList<WorkoutItem>(),state.workouts)
            }

        }

        @Test
        fun `When workouts are available , uiState workouts is populated`() = runTest(dispatcher) {
            every { getWorkoutsUseCase.invoke() } returns flow { emit(WorkoutStubWithTwoItems.workoutListStub) }

            viewmodel = MyWorkoutsViewmodel(
                getWorkoutsUseCase = getWorkoutsUseCase,
                mapper = WorkoutItemMapper()
            )

            viewmodel.uiState.test {
                assertInstanceOf(
                    MyWorkoutsUiState.Loading::class.java, awaitItem()
                )

                val state = assertInstanceOf(
                    MyWorkoutsUiState.Success::class.java, awaitItem()
                )

                assertEquals(WorkoutStubWithTwoItems.workoutItemListExpected,state.workouts)
            }
        }

        @Test
        fun `When workouts are update , uiState workouts are updated`() =
            runTest(dispatcher) {
                val workoutsFlow =
                    MutableStateFlow<List<Workout>>(WorkoutStubWithOneItem.workoutListStub)
                every { getWorkoutsUseCase.invoke() } returns workoutsFlow

                viewmodel = MyWorkoutsViewmodel(
                    getWorkoutsUseCase = getWorkoutsUseCase,
                    mapper = WorkoutItemMapper()
                )


                viewmodel.uiState.test {
                    assertInstanceOf(
                        MyWorkoutsUiState.Loading::class.java, awaitItem()
                    )
                    val firstState = assertInstanceOf(
                        MyWorkoutsUiState.Success::class.java, awaitItem()
                    )

                    assertEquals(WorkoutStubWithOneItem.workoutItemListExpected,firstState.workouts)

                    workoutsFlow.value = WorkoutStubWithTwoItems.workoutListStub

                    val secondState = assertInstanceOf(
                        MyWorkoutsUiState.Success::class.java, awaitItem()
                    )

                    assertEquals(WorkoutStubWithTwoItems.workoutItemListExpected,secondState.workouts)
                }
            }

        @Test
        fun `When workouts throw exception , uiState error is populated`() = runTest(dispatcher) {
            every { getWorkoutsUseCase.invoke() } returns flow { throw IOException("Something when wrong") }

            viewmodel = MyWorkoutsViewmodel(
                getWorkoutsUseCase = getWorkoutsUseCase,
                mapper = WorkoutItemMapper()
            )

            viewmodel.uiState.test {
                assertInstanceOf(
                    MyWorkoutsUiState.Loading::class.java, awaitItem()
                )
                assertEquals(
                    MyWorkoutsUiState.Error("Something when wrong"),
                    awaitItem()
                )
            }
        }
    }

    @Nested
    @DisplayName("navigation")
    inner class Navigation {
        @Test
        fun `when play is clicked, StartTimer is emitted`() = runTest(dispatcher) {
            every { getWorkoutsUseCase.invoke() } returns emptyFlow()
            viewmodel = MyWorkoutsViewmodel(
                getWorkoutsUseCase,
                WorkoutItemMapper()
            )

            viewmodel.uiEffect.test {
                viewmodel.onAction(
                    MyWorkoutsAction.PlayClicked(
                        WorkoutItem(
                            id = 1L,
                            null,
                            0L,
                            0L,
                            0
                        )
                    )
                )
                val effect =
                    assertInstanceOf(MyWorkoutsUiEffect.StartWorkout::class.java, awaitItem())
                assertEquals(1, effect.workoutId)
            }
        }

        @Test
        fun `when Row is clicked, NavigateEditWorkout is emitted`() = runTest(dispatcher) {
            every { getWorkoutsUseCase.invoke() } returns emptyFlow()
            viewmodel = MyWorkoutsViewmodel(
                getWorkoutsUseCase,
                WorkoutItemMapper()
            )

            viewmodel.uiEffect.test {
                viewmodel.onAction(
                    MyWorkoutsAction.RowClicked(
                        WorkoutItem(
                            id = 1L,
                            null,
                            0L,
                            0L,
                            0
                        )
                    )
                )
                val effect = assertInstanceOf(
                    MyWorkoutsUiEffect.NavigateEditWorkout::class.java,
                    awaitItem()
                )
                assertEquals(1, effect.workoutId)
            }
        }

        @Test
        fun `when floating button is clicked, NavigateAddWorkout is emitted`() =
            runTest(dispatcher) {
                every { getWorkoutsUseCase.invoke() } returns emptyFlow()
                viewmodel = MyWorkoutsViewmodel(
                    getWorkoutsUseCase,
                    WorkoutItemMapper()
                )

                viewmodel.uiEffect.test {
                    viewmodel.onAction(MyWorkoutsAction.AddWorkoutClicked)
                    assertInstanceOf(MyWorkoutsUiEffect.NavigateAddWorkout::class.java, awaitItem())

                }
            }
    }
}