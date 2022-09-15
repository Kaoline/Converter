package kaoline.converter.domain

import io.mockk.coEvery
import io.mockk.mockk
import kaoline.converter.domain.model.Amount
import kaoline.converter.domain.model.ConversionRate
import kaoline.converter.domain.model.ConverterError
import kaoline.converter.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ConvertAmountUseCaseTest {

    companion object {
        private const val EUR = "EUR"
        private const val USD = "USD"
        private const val JPY = "JPY"
    }

    private val ratesRepository = mockk<IRatesRepository>()
    private val convertAmountUseCase = ConvertAmountUseCase(ratesRepository)

    private suspend fun getConversionSuccess(amount: Amount): List<Amount> {
        return (convertAmountUseCase.performAction(amount)
            .first { it is Result.Success } as Result.Success).data
    }

    @Test
    fun `First result is Loading`() = runTest {
        // When
        val result = convertAmountUseCase.performAction(Amount(0f, USD)).first()

        // Then
        Assert.assertTrue(result is Result.Loading)
    }

    @Test
    fun `Convert 0 USD to EUR`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(ConversionRate(EUR, 1.58f))

        // When
        val converted = getConversionSuccess(Amount(0f, USD))

        // Then
        Assert.assertEquals(Amount(0f, EUR), converted[0])
    }

    @Test
    fun `Convert 1 USD to EUR`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(ConversionRate(EUR, 1.58f))

        // When
        val converted = getConversionSuccess(Amount(1f, USD))

        // Then
        Assert.assertEquals(Amount(1.58f, EUR), converted[0])
    }

    @Test
    fun `Convert 12345,89 USD to EUR`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(ConversionRate(EUR, 1.58f))

        // When
        val converted = getConversionSuccess(Amount(12345.89f, USD))

        // Then
        Assert.assertEquals(Amount(19506.506f, EUR), converted[0])
    }

    @Test
    fun `Convert -25 USD to EUR`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(ConversionRate(EUR, 1.58f))

        // When
        val converted = getConversionSuccess(Amount(-25f, USD))

        // Then
        Assert.assertEquals(Amount(-39.5f, EUR), converted[0])
    }

    @Test
    fun `Convert 58 EUR to USD`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(ConversionRate(EUR, 1.58f))

        // When
        val converted = getConversionSuccess(Amount(58f, EUR)).find { it.currency == USD }

        // Then
        Assert.assertEquals(Amount(36.70886f, USD), converted)
    }

    @Test
    fun `Convert 18547 JPY to EUR`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(
            ConversionRate(EUR, 1.58f),
            ConversionRate(JPY, 129f)
        )

        // When
        val converted = getConversionSuccess(Amount(18547f, JPY)).first { it.currency == EUR }

        // Then
        Assert.assertEquals(Amount(227.16481f, EUR), converted)
    }

    @Test
    fun `When no rate is available, return NoRateAvailableError`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns emptyList<ConversionRate>()

        // When
        val result =
            convertAmountUseCase.performAction(Amount(58f, EUR)).first { it !is Result.Loading }

        // Then
        Assert.assertTrue(result is Result.Failure)
        Assert.assertEquals(ConverterError.NoRateAvailableError, (result as Result.Failure).error)
    }

    @Test
    fun `Convert to all available rates and start currency`() = runTest {
        // Given
        coEvery { ratesRepository.getRates() } returns listOf(
            ConversionRate(EUR, 1.58f),
            ConversionRate(JPY, 129f)
        )

        // When
        val converted = getConversionSuccess(Amount(58f, USD))

        // Then
        Assert.assertEquals(3, converted.size)
        Assert.assertTrue(converted.any { it.currency == EUR })
        Assert.assertTrue(converted.any { it.currency == JPY })
    }
}
