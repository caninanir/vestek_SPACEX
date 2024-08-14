package unittest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import kotlinx.coroutines.test.runTest
import io.mockk.*
import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.repository.SpaceXRepository
import com.caninanir.spacex.domain.usecase.FetchLaunchpadByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketsUseCase
import com.caninanir.spacex.domain.usecase.FetchUpcomingLaunchesUseCase

@ExperimentalCoroutinesApi
class UseCaseTests {

    @Test
    fun `FetchLaunchpadByIdUseCase fetches launchpad`() = runTest {
        val launchpadId = "5e9e4501f5090910d4566f83"
        val mockLaunchpad = mockk<Launchpad>()
        val repository = mockk<SpaceXRepository> {
            coEvery { getLaunchpadById(launchpadId) } returns mockLaunchpad
        }
        val useCase = FetchLaunchpadByIdUseCase(repository)

        val result = useCase.invoke(launchpadId)

        Assert.assertEquals(mockLaunchpad, result)
    }

    @Test
    fun `FetchRocketByIdUseCase fetches rocket`() = runTest {
        val rocketId = "5e9d0d95eda69955f709d1eb"
        val mockRocket = mockk<Rocket>()
        val repository = mockk<SpaceXRepository> {
            coEvery { getRocketById(rocketId) } returns mockRocket
        }
        val useCase = FetchRocketByIdUseCase(repository)

        val result = useCase.invoke(rocketId)

        Assert.assertEquals(mockRocket, result)
    }

    @Test
    fun `FetchRocketsUseCase fetches rockets`() = runTest {
        val mockRockets = listOf(mockk<Rocket>(), mockk<Rocket>())
        val repository = mockk<SpaceXRepository> {
            coEvery { getRockets() } returns mockRockets
        }
        val useCase = FetchRocketsUseCase(repository)

        val result = useCase.invoke()

        Assert.assertEquals(mockRockets, result)
    }

    @Test
    fun `FetchUpcomingLaunchesUseCase fetches upcoming launches`() = runTest {
        val mockLaunches = listOf(mockk<Launch>(), mockk<Launch>())
        val repository = mockk<SpaceXRepository> {
            coEvery { getUpcomingLaunches() } returns mockLaunches
        }
        val useCase = FetchUpcomingLaunchesUseCase(repository)

        val result = useCase.invoke()

        Assert.assertEquals(mockLaunches, result)
    }
}