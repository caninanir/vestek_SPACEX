package unittest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.*
import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.usecase.FetchLaunchpadByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketsUseCase
import com.caninanir.spacex.domain.usecase.FetchUpcomingLaunchesUseCase
import com.caninanir.spacex.presentation.viewmodel.LaunchpadDetailViewModel
import com.caninanir.spacex.presentation.viewmodel.RocketDetailViewModel
import com.caninanir.spacex.presentation.viewmodel.RocketListViewModel
import com.caninanir.spacex.presentation.viewmodel.UpcomingLaunchesViewModel
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class ViewModelsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `LaunchpadDetailViewModel fetches launchpad details`() = runTest {
        val launchpadId = "5e9e4501f5090910d4566f83"
        val mockLaunchpad = mockk<Launchpad>()
        val fetchLaunchpadByIdUseCase = mockk<FetchLaunchpadByIdUseCase> {
            coEvery { this@mockk.invoke(launchpadId) } returns mockLaunchpad
        }
        val viewModel = LaunchpadDetailViewModel(fetchLaunchpadByIdUseCase)

        var fetchedLaunchpad: Launchpad? = null
        viewModel.fetchLaunchpadById(launchpadId) { launchpad -> fetchedLaunchpad = launchpad }
        advanceUntilIdle()

        assertEquals(mockLaunchpad, fetchedLaunchpad)
    }

    @Test
    fun `RocketDetailViewModel fetches rocket details`() = runTest {
        val rocketId = "5e9d0d95eda69955f709d1eb"
        val mockRocket = mockk<Rocket>()
        val fetchRocketByIdUseCase = mockk<FetchRocketByIdUseCase> {
            coEvery { this@mockk.invoke(rocketId) } returns mockRocket
        }
        val viewModel = RocketDetailViewModel(fetchRocketByIdUseCase)

        var fetchedRocket: Rocket? = null
        viewModel.fetchRocketById(rocketId) { rocket -> fetchedRocket = rocket }
        advanceUntilIdle()

        assertEquals(mockRocket, fetchedRocket)
    }

    @Test
    fun `RocketListViewModel fetches rockets`() = runTest {
        val mockRockets = listOf(mockk<Rocket>(), mockk<Rocket>())
        val fetchRocketsUseCase = mockk<FetchRocketsUseCase> {
            coEvery { this@mockk.invoke() } returns mockRockets
        }
        val viewModel = RocketListViewModel(fetchRocketsUseCase)
        advanceUntilIdle()

        assertEquals(mockRockets, viewModel.rockets.value)
    }

    @Test
    fun `UpcomingLaunchesViewModel fetches upcoming launches`() = runTest {
        val mockLaunches = listOf(mockk<Launch>(), mockk<Launch>())
        val fetchUpcomingLaunchesUseCase = mockk<FetchUpcomingLaunchesUseCase> {
            coEvery { this@mockk.invoke() } returns mockLaunches
        }
        val viewModel = UpcomingLaunchesViewModel(fetchUpcomingLaunchesUseCase)
        advanceUntilIdle()

        assertEquals(mockLaunches, viewModel.upcomingLaunches.value)
    }
}