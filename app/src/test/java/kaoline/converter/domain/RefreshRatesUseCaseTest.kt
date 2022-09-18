package kaoline.converter.domain

import io.mockk.coVerify
import io.mockk.mockk
import kaoline.converter.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshRatesUseCaseTest {

    private val ratesRepository = mockk<IRatesRepository>(relaxed = true)
    private val refreshRatesUseCase = RefreshRatesUseCase(ratesRepository)

    @Test
    fun `First result is Loading`() = runTest {
        // When
        val result = refreshRatesUseCase.performAction().first()

        // Then
        Assert.assertTrue(result is Result.Loading)
    }

    @Test
    fun `Refresh rates`() = runTest {
        // Given

        // When
        refreshRatesUseCase.performAction().collect()

        // Then
        coVerify { ratesRepository.refreshRates(any()) }
    }

    @Test
    fun `Refresh rates with a 30 min interval`() = runTest {
        // Given

        val interval = 30 * 60 * 1000L

        // When
        refreshRatesUseCase.performAction().collect()

        // Then
        coVerify { ratesRepository.refreshRates(interval) }
    }
}
