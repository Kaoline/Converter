package kaoline.converter.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kaoline.converter.data.cache.RateDao
import kaoline.converter.data.cache.model.RateEntity
import kaoline.converter.data.network.RatesApiService
import kaoline.converter.data.network.model.RatesResponse
import kaoline.converter.domain.model.ConverterError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.time.Instant
import java.util.*

@ExperimentalCoroutinesApi
class RatesRepositoryTest {

    private val ratesApiService = mockk<RatesApiService>()
    private val rateDao = mockk<RateDao>(relaxUnitFun = true)
    private val ratesRepository = RatesRepository(ratesApiService, rateDao)

    private val standardApiResponse = Response.success(
        RatesResponse(mapOf(Pair("EUR", 1.58f)))
    )

    @Test
    fun `Get data from api service when cache is not populated`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns emptyList()
        coEvery { ratesApiService.getRates() } returns standardApiResponse

        // When
        ratesRepository.getRates()

        // Then
        coVerify(atLeast = 1) { rateDao.getAllRates() }
        coVerify(exactly = 1) { ratesApiService.getRates() }
    }

    @Test
    fun `Store data in cache when it is not populated`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns emptyList()
        coEvery { ratesApiService.getRates() } returns standardApiResponse

        // When
        ratesRepository.getRates()

        // Then
        coVerify(exactly = 1) { rateDao.insertAll(any()) }
    }

    @Test
    fun `Get data from cache when it is populated`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns listOf(RateEntity("EUR", 1.58f))

        // When
        ratesRepository.getRates()

        // Then
        coVerify(exactly = 1) { rateDao.getAllRates() }
        coVerify(exactly = 0) { ratesApiService.getRates() }
    }

    @Test(expected = ConverterError.RefreshTooEarly::class)
    fun `Do not refresh data younger than 40 min`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns listOf(RateEntity("EUR", 1.58f))

        // When
        ratesRepository.refreshRates(40 * 60 * 1000)

        // Then
        coVerify(exactly = 0) { ratesApiService.getRates() }
    }

    @Test
    fun `Refresh data older than 30 min`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns listOf(
            RateEntity(
                "EUR",
                1.58f,
                Date.from(Instant.now().minusSeconds(1900))
            )
        )
        coEvery { ratesApiService.getRates() } returns standardApiResponse

        // When
        ratesRepository.refreshRates(30 * 60 * 1000)

        // Then
        coVerify(exactly = 1) { ratesApiService.getRates() }
    }

    @Test
    fun `Populate cache when data is correct`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns emptyList()
        coEvery { ratesApiService.getRates() } returns standardApiResponse

        // When
        ratesRepository.getRates()

        // Then
        coVerify(exactly = 1) { rateDao.insertAll(any()) }
    }

    @Test(expected = ConverterError.ApiError::class)
    fun `Error when no cached data and network error`() = runTest {
        // Given
        every { rateDao.getAllRates() } returns emptyList()
        coEvery { ratesApiService.getRates() } returns Response.error(
            500,
            "Some random error".toResponseBody()
        )

        // When
        ratesRepository.getRates()
    }
}