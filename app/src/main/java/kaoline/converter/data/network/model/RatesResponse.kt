package kaoline.converter.data.network.model

/**
 * Represents the rates return from OpenExchange API as an object. We use only the "rate" parameter.
 * For example: "JPY: 158"
 */
data class RatesResponse(
    val rates: Map<String, Float>
)
