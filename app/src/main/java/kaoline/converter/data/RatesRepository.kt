package kaoline.converter.data

import kaoline.converter.data.cache.RateDao
import kaoline.converter.data.network.RatesApiService
import kaoline.converter.domain.IRatesRepository
import kaoline.converter.domain.model.ConversionRate

class RatesRepository(
    private val ratesApiService: RatesApiService,
    private val rateDao: RateDao
) : IRatesRepository {
    override suspend fun getRates(): List<ConversionRate> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshRates() {
        TODO("Not yet implemented")
    }
}
