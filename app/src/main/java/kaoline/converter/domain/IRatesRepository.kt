package kaoline.converter.domain

import kaoline.converter.domain.model.ConversionRate

interface IRatesRepository {
    suspend fun getRates(): List<ConversionRate>
    suspend fun refreshRates()
}
