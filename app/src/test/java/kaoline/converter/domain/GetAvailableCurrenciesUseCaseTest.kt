package kaoline.converter.domain

import io.mockk.every
import io.mockk.mockk
import kaoline.converter.domain.model.ConversionRate
import kaoline.converter.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAvailableCurrenciesUseCaseTest {

    private val ratesRepository = mockk<IRatesRepository>()
    private val getAvailableCurrenciesUseCase = GetAvailableCurrenciesUseCase(ratesRepository)

    private suspend fun getRatesSuccess(): List<String> {
        return (getAvailableCurrenciesUseCase.performAction()
            .first { it is Result.Success } as Result.Success).data
    }

    @Test
    fun `First result is Loading`() = runTest {
        // When
        val result = getAvailableCurrenciesUseCase.performAction().first()

        // Then
        Assert.assertTrue(result is Result.Loading)
    }

    @Test
    fun `Get all available currencies and USD`() = runTest {
        // Given
        every { ratesRepository.getRates() } returns listOf(
            ConversionRate("EUR", 1.58f),
            ConversionRate("JPY", 156f),
            ConversionRate("ABC", 123f)
        )

        // When
        val currencies = getRatesSuccess()

        // Then
        Assert.assertEquals(4, currencies.size)
        Assert.assertTrue(currencies.contains("EUR"))
        Assert.assertTrue(currencies.contains("JPY"))
        Assert.assertTrue(currencies.contains("ABC"))
    }

    @Test
    fun `When no rate is available, return at least USD`() = runTest {
        // Given
        every { ratesRepository.getRates() } returns emptyList()

        // When
        val currencies = getRatesSuccess()

        // Then
        Assert.assertEquals(1, currencies.size)
    }
}
