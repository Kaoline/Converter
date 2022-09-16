package kaoline.converter.data

import kaoline.converter.data.cache.model.RateEntity
import kaoline.converter.domain.model.ConversionRate

fun RateEntity.toConversionRate() = ConversionRate(toCurrency = currency, rate = rate)
fun List<RateEntity>.toConversionRate() = this.map { it.toConversionRate() }
