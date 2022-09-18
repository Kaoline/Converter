package kaoline.converter.data

import kaoline.converter.data.base.BaseRepository
import kaoline.converter.data.cache.RateDao
import kaoline.converter.data.cache.model.RateEntity
import kaoline.converter.data.network.RatesApiService
import kaoline.converter.domain.IRatesRepository
import kaoline.converter.domain.model.ConversionRate
import kaoline.converter.domain.model.ConverterError
import java.time.Instant
import java.util.*

class RatesRepository(
    private val ratesApiService: RatesApiService, private val rateDao: RateDao
) : BaseRepository(), IRatesRepository {
    /**
     * Get rates from the database, populate it first from OpenExchange API if needed.
     */
    override suspend fun getRates(): List<ConversionRate> {
        var cacheData = rateDao.getAllRates()
        if (cacheData.isEmpty()) {
            fetchRatesAndPopulateDatabase()
            cacheData = rateDao.getAllRates()
        }
        return cacheData.toConversionRate()
    }

    /**
     * Refresh rates if the last refresh was before a certain time.
     */
    override suspend fun refreshRates(minRefreshIntervalMs: Long) {
        val intervalAgo = Date.from(Instant.now().minusMillis(minRefreshIntervalMs))
        val lastModified = rateDao.getAllRates().firstOrNull()?.lastModified ?: Date(0)
        if (lastModified.before(intervalAgo)) {
            fetchRatesAndPopulateDatabase()
        } else {
            throw ConverterError.RefreshTooEarly(lastModified.time - Date().time + minRefreshIntervalMs)
        }
    }

    private suspend fun fetchRatesAndPopulateDatabase() {
        handleErrorsCall { ratesApiService.getRates() }?.let { ratesResponse ->
            val rateEntities =
                ratesResponse.rates.map { RateEntity(currency = it.key, rate = it.value) }
            rateDao.insertAll(rateEntities)
        }
    }
}
