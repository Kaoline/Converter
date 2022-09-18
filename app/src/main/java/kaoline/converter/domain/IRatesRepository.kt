package kaoline.converter.domain

import kaoline.converter.domain.model.ConversionRate
import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow

interface IRatesRepository {
    suspend fun getRates(): List<ConversionRate>
    suspend fun refreshRates(minRefreshIntervalMs: Long)
}
