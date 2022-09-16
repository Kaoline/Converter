package kaoline.converter.data

import kaoline.converter.data.base.BaseRepository
import kaoline.converter.data.cache.RateDao
import kaoline.converter.data.cache.model.RateEntity
import kaoline.converter.data.network.RatesApiService
import kaoline.converter.domain.IRatesRepository
import kaoline.converter.domain.model.ConversionRate

class RatesRepository(
    private val ratesApiService: RatesApiService,
    private val rateDao: RateDao
) : BaseRepository(), IRatesRepository {
    override suspend fun getRates(): List<ConversionRate> {
        var cacheData = rateDao.getAllRates()
        if (cacheData.isEmpty()) {
            fetchRatesAndPopulateDatabase()
            cacheData = rateDao.getAllRates()
        }
        return cacheData.toConversionRate()
    }

    override suspend fun refreshRates() {
        TODO("Not yet implemented")
    }

    private suspend fun fetchRatesAndPopulateDatabase() {
        handleErrorsCall { ratesApiService.getRates() }?.let { ratesResponse ->
            val rateEntities =
                ratesResponse.rates.map { RateEntity(currency = it.key, rate = it.value) }
            rateDao.insertAll(rateEntities)
        }
    }
}
