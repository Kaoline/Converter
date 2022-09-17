package kaoline.converter.domain.model

/**
 * Represents an amount with its currency. For example: 3USD, or -58JPY
 */
data class Amount(
    val amountValue: Float,
    val currency: String
) {
    companion object {
        const val USD = "USD"
    }
}
