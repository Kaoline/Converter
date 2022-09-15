package kaoline.converter.domain.model

/**
 * Represents a conversion rate from USD
 */
data class ConversionRate(
    val toCurrency: String,
    val rate: Float
)
