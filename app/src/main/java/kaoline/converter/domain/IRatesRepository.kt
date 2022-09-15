package kaoline.converter.domain

import kaoline.converter.domain.model.ConversionRate

interface IRatesRepository {
    fun getRates(): List<ConversionRate>
}
